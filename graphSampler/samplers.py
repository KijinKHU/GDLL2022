import string
import networkx as nx
import numpy as np


class Sampler(object):

    def sample_mfg(self, graph, indices):
        raise NotImplementedError

    def sample(self, graph, indices):
        raise NotImplementedError


class NeighborSamplerByNode(Sampler):

    def __init__(self, fanouts, edge_dir='in', prob=None, mask=None, replace=False):
        super().__init__()
        if isinstance(fanouts, list):
            self.fanouts = fanouts
        elif isinstance(fanouts, string):
            string_list = fanouts.split(',')
            self.fanouts = list(map(int, string_list))

        self.edge_dir = edge_dir
        self.MFGs = []

    def sample_mfg(self, graph, nodes, fanout, replace):
        MFG = nx.Graph()
        src_nodes = []
        dst_nodes = []
        for node in nodes:
            edges = graph.getEdges(node)
            neighbors = [e[1] for e in edges]
            sampled_neighbors = np.random.choice(neighbors, fanout)
            # print(sampled_neighbors)
            src = [node] * fanout
            # print(src)
            src_nodes.extend(src)
            dst = sampled_neighbors
            # print(dst)
            dst_nodes.extend(dst)
            for i in range(fanout):
                MFG.add_edge(src[i], dst[i])

        return [src_nodes, dst_nodes, MFG]

    def sample(self, graph, nodes):
        source_nodes = nodes
        for fanout in self.fanouts:
            MFG = self.sample_mfg(graph, source_nodes, fanout, replace=True)
            self.MFGs.append(MFG)
            source_nodes = MFG[1]

        return self.MFGs
