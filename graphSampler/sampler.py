from .base import BlockSampler
from dgl.base import NID, EID
from dgl.transforms import to_block
from dgl.distributed import dist_graph
import numpy as np
import torch as th

class NeighborSampler(BlockSampler):
    def __init__(self, fanouts, edge_dir='in', prob=None, mask=None, replace=False,
                 prefetch_node_feats=None, prefetch_labels=None, prefetch_edge_feats=None,
                 output_device=None):
        super().__init__(prefetch_node_feats=prefetch_node_feats,
                         prefetch_labels=prefetch_labels,
                         prefetch_edge_feats=prefetch_edge_feats,
                         output_device=output_device)
        self.fanouts = fanouts
        self.edge_dir = edge_dir
        if mask is not None and prob is not None:
            raise ValueError(
                'Mask and probability arguments are mutually exclusive. '
                'Consider multiplying the probability with the mask '
                'to achieve the same goal.')
        self.prob = prob or mask
        self.replace = replace

    def sample_blocks(self, g, seed_nodes, exclude_eids=None):
        output_nodes = seed_nodes
        blocks = []
        for fanout in reversed(self.fanouts):
            frontier = g.sample_neighbors(
                seed_nodes, fanout, edge_dir=self.edge_dir, prob=self.prob,
                replace=self.replace, output_device=self.output_device,
                exclude_edges=exclude_eids)
            eid = frontier.edata[EID]
            block = to_block(frontier, seed_nodes)
            block.edata[EID] = eid
            seed_nodes = block.srcdata[NID]
            blocks.insert(0, block)

        return seed_nodes, output_nodes, blocks

    def sample_blocks(self, dist_g, seed_nodes, exclude_eids=None):
        seeds = th.LongTensor(np.asarray(seed_nodes))
        seed_nodes, output_nodes, blocks = self.sample_neighbors_dfs(
            self.g, seeds, self.fanouts, replace=True
        )
        return seed_nodes, output_nodes, blocks

MultiLayerNeighborSampler = NeighborSampler

class MultiLayerFullNeighborSampler(NeighborSampler):

    def __init__(self, num_layers, **kwargs):
        super().__init__([-1] * num_layers, **kwargs)
