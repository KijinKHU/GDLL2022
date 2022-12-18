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
        edges_list = graph.GetEdges(nodes)
        # mfg = graph.GetNeighbors(nodes)
        for node in nodes:
            edges = graph.getEdges(node)
            num_edges = len(edges)
            neighbors = [e[0] for e in edges]
            sampled_neighbors = np.random.choice(neighbors, fanout)
            print(sampled_neighbors)
        return edges_list

    def sample(self, graph, nodes):
        source_nodes = nodes
        for fanout in self.fanouts:
            source_idices, mfg = self.sample_mfg(graph, nodes, fanout, replace=True)
            self.MFGs.append(mfg)

        return self.MFGs
