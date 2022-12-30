# -*- coding: utf-8 -*-

from __future__ import print_function
from __future__ import unicode_literals
from __future__ import division
from __future__ import absolute_import

import torch
import torch.nn as nn
import torch.nn.functional as F
from torch.autograd import Function
import math
from torch_geometric.nn.conv import MessagePassing, SAGEConv, GraphConv
from torch_geometric.utils import add_self_loops, degree
from torch.nn import Linear
import torch_sparse
from torch_scatter import scatter_add

### This class has been created to provide Binary graph convolutional layer from the paper "Bi-GCN: Binary Graph Convolutional Network",
#It provides binarized graph convolutional operation by aggregating as mean aggregation and then combining by Relu.
# date created = 20 Oct, 2022, Project name : GDBMs, created by: Tariq Habib Afridi, email address: afridi@khu.ac.kr


def where(cond, x1, x2):
    return cond.float() * x1 + (1 - cond.float()) * x2


class BinLinearFunction(Function):
    @staticmethod
    def forward(ctx, input, weight, bias=None):
        ctx.save_for_backward(input, weight, bias)
        # size of input: [n, in_channels]
        # size of weight: [out_channels, in_channels]
        # size of bias: [out_channels]

        s = weight.size()
        n = s[1]
        m = weight.norm(1, dim=1, keepdim=True).div(n)
        weight_hat = weight.sign().mul(m.expand(s))
        output = input.mm(weight_hat.t())

        if bias is not None:
            output += bias.unsqueeze(0).expand_as(output)
        return output

    @staticmethod
    def backward(ctx, grad_output):
        input, weight, bias = ctx.saved_variables
        grad_input = grad_bias = None

        grad_weight = grad_output.t().mm(input)

        s = weight.size()
        n = s[1]
        m = weight.norm(1, dim=1, keepdim=True).div(n).expand(s)
        # print(m.shape, m)
        m[weight.lt(-1.0)] = 0
        m[weight.gt(1.0)] = 0
        m = m.mul(grad_weight)

        m_add = weight.sign().mul(grad_weight)
        m_add = m_add.sum(dim=1, keepdim=True).expand(s)
        m_add = m_add.mul(weight.sign()).div(n)

        if ctx.needs_input_grad[0]:
            grad_input = grad_output.mm(weight.sign())
        if ctx.needs_input_grad[1]:
            grad_weight = m.add(m_add)
            # grad_weight[weight.lt(-1.0)] = 0
            # grad_weight[weight.gt(1.0)] = 0
        if bias is not None and ctx.needs_input_grad[2]:
            grad_bias = grad_output.sum(0).squeeze(0)
        return grad_input, grad_weight, grad_bias


class BinActiveFunction(Function):
    @staticmethod
    def forward(ctx, input):
        ctx.save_for_backward(input)
        # size of input: [n, in_channels]

        input_b = input.sign()
        s = input.size()
        alpha = input.norm(1, 1, keepdim=True).div(s[1]).expand(s)
        output = alpha.mul(input_b)

        return output

    @staticmethod
    def backward(ctx, grad_output):
        input = ctx.saved_variables
        grad_input = grad_output.clone()

        # grad_input = m.add(m_add)
        grad_input = grad_input * where(torch.abs(input[0]) <= 1, 1, 0)

        return grad_input


BinLinearFun = BinLinearFunction.apply
BinActiveFun = BinActiveFunction.apply


class BinLinear(torch.nn.Module):
    def __init__(self, in_features, out_features, bias=True):
        super(BinLinear, self).__init__()
        self.in_features = in_features
        self.out_features = out_features
        self.weight = torch.nn.Parameter(torch.Tensor(out_features, in_features))
        if bias:
            self.bias = torch.nn.Parameter(torch.Tensor(out_features))
        else:
            self.register_parameter('bias', None)
        self.reset_parameters()

    def reset_parameters(self):
        self.weight.data.normal_(0, 1 * (math.sqrt(1. / self.in_features)))
        if self.bias is not None:
            self.bias.data.zero_()

    def forward(self, input):

        return BinLinearFun(input, self.weight, self.bias)

    def __repr__(self):
        return self.__class__.__name__ + ' (' \
            + str(self.in_features) + ' -> ' \
            + str(self.out_features) + ')'


class BinActive(torch.nn.Module):
    def __init__(self):
        super(BinActive, self).__init__()

    def forward(self, input):

        return BinActiveFun(input)

    def __repr__(self):
        return self.__class__.__name__ + ' ()'


class BiGCNConv(MessagePassing):
    def __init__(self, in_channels, out_channels, cached=True, bi=False):
        super(BiGCNConv, self).__init__(aggr="add")
        self.cached = cached
        self.bi = bi
        if bi:
            self.lin = BinLinear(in_channels, out_channels)
        else:
            self.lin = torch.nn.Linear(in_channels, out_channels)

        self.reset_parameters()

    def reset_parameters(self):
        self.lin.reset_parameters()
        self.cached_result = None

    def forward(self, x, edge_index):
        x = self.lin(x)

        if not self.cached or self.cached_result is None:
            edge_index, _ = add_self_loops(edge_index, num_nodes=x.size(0))

            # Compute normalization
            row, col = edge_index
            deg = degree(row, x.size(0), dtype=x.dtype)
            deg_inv_sqrt = deg.pow(-0.5)
            # normalization of each edge
            norm = deg_inv_sqrt[row] * deg_inv_sqrt[col]

            self.cached_result = edge_index, norm

        edge_index, norm = self.cached_result

        x = self.propagate(edge_index,size=(x.size(0), x.size(0)),
                              x=x, norm=norm)
        return x

    def message(self, x_j, norm):

        # Normalize node features
        return norm.view(-1, 1) * x_j


class indBiGCNConv(MessagePassing):
    def __init__(self, in_channels, out_channels, binarize=False):
        super(indBiGCNConv, self).__init__(aggr="mean")
        self.in_channels = in_channels
        self.out_channels = out_channels
        self.binarize = binarize
        if binarize:
            self.lin = BinLinear(in_channels, out_channels)
        else:
            self.lin = torch.nn.Linear(in_channels, out_channels)

        self.reset_parameters()

    def reset_parameters(self):
        self.lin.reset_parameters()

    def forward(self, x, edge_index):
        # shape of x: [N, in_channels]
        # shape of edge_index: [2, E]
        if torch.is_tensor(x):
            x = (x, x)

        out = self.propagate(edge_index, x=x, norm=None)
        out = self.lin(out)

        return out


class BiSAGEConv(MessagePassing):
    def __init__(self, in_channels, out_channels, normalize=False, binarize=False,
                 **kwargs):
        super(BiSAGEConv, self).__init__(aggr='mean', **kwargs)

        self.in_channels = in_channels
        self.out_channels = out_channels
        self.normalize = normalize
        if binarize:
            self.lin_rel = BinLinear(in_channels, out_channels)
            self.lin_root = BinLinear(in_channels, out_channels)
        else:
            self.lin_rel = Linear(in_channels, out_channels, bias=True)
            self.lin_root = Linear(in_channels, out_channels, bias=True)

        self.reset_parameters()

    def reset_parameters(self):
        self.lin_rel.reset_parameters()
        self.lin_root.reset_parameters()

    def forward(self, x, edge_index):
        """"""

        if torch.is_tensor(x):
            x = (x, x)

        out = self.propagate(edge_index, x=x)
        out = self.lin_rel(out) + self.lin_root(x[1])

        if self.normalize:
            out = F.normalize(out, p=2, dim=-1)
        return out


class BiGraphConv(MessagePassing):
    def __init__(self, in_channels, out_channels, binarize=False, aggr='add', bias=True,
                 **kwargs):
        super(BiGraphConv, self).__init__(aggr=aggr, **kwargs)

        self.in_channels = in_channels
        self.out_channels = out_channels
        if binarize:
            self.lin = BinLinear(in_channels, out_channels)
            self.lin_root = BinLinear(in_channels, out_channels)
        else:
            self.lin = torch.nn.Linear(in_channels, out_channels, bias=bias)
            self.lin_root = torch.nn.Linear(in_channels, out_channels, bias=bias)

        self.reset_parameters()

    def reset_parameters(self):
        self.lin.reset_parameters()
        self.lin_root.reset_parameters()

    @staticmethod
    def norm(edge_index, num_nodes, edge_weight=None, dtype=None):
        if edge_weight is None:
            edge_weight = torch.ones((edge_index.size(1),), dtype=dtype,
                                     device=edge_index.device)
        row, col = edge_index
        deg = scatter_add(edge_weight, row, dim=0, dim_size=num_nodes)
        deg_inv_sqrt = deg.pow(-0.5)
        deg_inv_sqrt[deg_inv_sqrt == float('inf')] = 0

        return edge_index, deg_inv_sqrt[row] * edge_weight * deg_inv_sqrt[col]

    def forward(self, x, edge_index, edge_weight=None, size=None):
        """"""
        h = self.lin(x)
        edge_index, edge_weight = self.norm(edge_index, x.size(0))
        return self.propagate(edge_index, size=size, x=x, h=h,
                              edge_weight=edge_weight)

    def propagate(self, edge_index, size, x, h, edge_weight):

        # message and aggregate
        if size is None:
            size = [x.size(0), x.size(0)]

        adj = torch_sparse.SparseTensor(row=edge_index[0], rowptr=None, col=edge_index[1], value=edge_weight,
                     sparse_sizes=torch.Size(size), is_sorted=True)  # is_sorted=True
        out = torch_sparse.matmul(adj, h, reduce='sum')
        # out = torch.cat([out, self.lin_root(x)], dim=1)
        out = out + self.lin_root(x)
        return out


# Initialization functions
def zeros_init(tensor):
    if tensor is not None:
        tensor.data.fill_(0)


def ones_init(tensor):
    if tensor is not None:
        tensor.data.fill_(1)


def glorot_init(tensor):
    if tensor is not None:
        stdv = math.sqrt(6.0 / (tensor.size(-2) + tensor.size(-1)))
        tensor.data.uniform_(-stdv, stdv)

class BiGCN(torch.nn.Module):
    def __init__(self, in_channels, hidden_channels, out_channels, layers, dropout, print_log=True):
        super(BiGCN, self).__init__()

        if print_log:
            print("Create a {:d}-layered Bi-GCN.".format(layers))

        self.layers = layers
        self.dropout = dropout
        self.bn1 = torch.nn.BatchNorm1d(in_channels, affine=False)

        convs = []
        for i in range(self.layers):
            in_dim = hidden_channels if i > 0 else in_channels
            out_dim = hidden_channels if i < self.layers - 1 else out_channels
            if print_log:
                print("Layer {:d}, in_dim {:d}, out_dim {:d}".format(i, in_dim, out_dim))
            convs.append(BiGCNConv(in_dim, out_dim, cached=True, bi=True))
        self.convs = torch.nn.ModuleList(convs)

        self.reset_parameters()

    def reset_parameters(self):
        for conv in self.convs:
            conv.reset_parameters()

    def forward(self, data):
        x, edge_index = data.x, data.edge_index

        x = self.bn1(x)

        for i, conv in enumerate(self.convs):
            x = BinActive()(x)
            x = F.dropout(x, p=self.dropout, training=self.training)
            x = conv(x, edge_index)

        return F.log_softmax(x, dim=1)


# indGCN and GraphSAGE
class NeighborSamplingGCN(torch.nn.Module):
    def __init__(self, model: str, in_channels, hidden_channels, out_channels, binarize, dropout=0.):
        super(NeighborSamplingGCN, self).__init__()

        assert model in ['indGCN', 'GraphSAGE'], 'Only indGCN and GraphSAGE are available.'
        GNNConv = indBiGCNConv if model == 'indGCN' else BiSAGEConv

        self.num_layers = 2
        self.model = model
        self.binarize = binarize
        self.dropout = dropout

        self.convs = torch.nn.ModuleList()
        self.convs.append(GNNConv(in_channels, hidden_channels, binarize=binarize))
        self.convs.append(GNNConv(hidden_channels, out_channels, binarize=binarize))
        self.reset_parameters()

    def reset_parameters(self):
        for conv in self.convs:
            conv.reset_parameters()

    def forward(self, x, adjs):

        for i, (edge_index, _, size) in enumerate(adjs):
            x_target = x[:size[1]]
            if(self.binarize):
                x = x - x.mean(dim=1, keepdim=True)
                x = x / (x.std(dim=1, keepdim=True) + 0.0001)
                x = BinActive()(x)

                x_target = x_target - x_target.mean(dim=1, keepdim=True)
                x_target = x_target / (x_target.std(dim=1, keepdim=True) + 0.0001)
                x_target = BinActive()(x_target)
            # if self.model == 'GraphSAGE':
            #     edge_index, _ = add_self_loops(edge_index, num_nodes=x[0].size(0))
            x = self.convs[i]((x, x_target), edge_index)
            if i != self.num_layers - 1:
                x = F.relu(x)
                x = F.dropout(x, p=self.dropout, training=self.training)
        return x.log_softmax(dim=-1)

    def inference(self, x_all, subgraph_loader, device):
        for i in range(self.num_layers):
            xs = []
            for batch_size, n_id, adj in subgraph_loader:
                edge_index, _, size = adj.to(device)
                x = x_all[n_id].to(device)
                x_target = x[:size[1]]
                if self.binarize:
                    # bn x
                    x = x - x.mean(dim=1, keepdim=True)
                    x = x / (x.std(dim=1, keepdim=True) + 0.0001)
                    x = BinActive()(x)

                    # bn x_target
                    x_target = x_target - x_target.mean(dim=1, keepdim=True)
                    x_target = x_target / (x_target.std(dim=1, keepdim=True) + 0.0001)
                    x_target = BinActive()(x_target)
                x = self.convs[i]((x, x_target), edge_index)
                if i != self.num_layers - 1:
                    x = F.relu(x)
                xs.append(x.cpu())

            x_all = torch.cat(xs, dim=0)

        return x_all


class SAINT(torch.nn.Module):
    def __init__(self, in_channels, hidden_channels, out_channels, dropout, binarize):
        super(SAINT, self).__init__()
        self.dropout = dropout
        self.binarize = binarize
        self.conv1 = BiGraphConv(in_channels, hidden_channels, binarize=self.binarize)
        self.conv2 = BiGraphConv(hidden_channels, hidden_channels, binarize=self.binarize)
        # if self.binarize:
        #     self.lin = BinLinear(2 * hidden_channels, out_channels)
        # else:
        self.lin = torch.nn.Linear(2 * hidden_channels, out_channels)

    def set_aggr(self, aggr):
        self.conv1.aggr = aggr
        self.conv2.aggr = aggr

    def reset_parameters(self):
        self.conv1.reset_parameters()
        self.conv2.reset_parameters()
        self.lin.reset_parameters()

    def forward(self, x0, edge_index, edge_weight=None):
        if self.binarize:
            x0 = x0 - x0.mean(dim=1, keepdim=True)
            x0 = x0 / (x0.std(dim=1, keepdim=True) + 0.0001)
            x0 = BinActive()(x0)
        x1 = self.conv1(x0, edge_index, edge_weight)
        if not self.binarize:
            x1 = F.relu(x1)
        x1 = F.dropout(x1, p=self.dropout, training=self.training)

        if self.binarize:
            x2 = BinActive()(x1)
        else:
            x2 = x1
        x2 = self.conv2(x2, edge_index, edge_weight)
        if not self.binarize:
            x2 = F.relu(x2)
        x2 = F.dropout(x2, p=self.dropout, training=self.training)

        x = torch.cat([x1, x2], dim=-1)
        x = self.lin(x)
        return x.log_softmax(dim=-1)