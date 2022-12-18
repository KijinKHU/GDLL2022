import string


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

    def sample_mfg(self, graph, node_indices, replace):
        mfg = graph.GetNeighbors(node_indices)
        return mfg

    def sample(self, graph, node_indices):
        source_idices = node_indices
        for fanout in self.fanouts:
            source_idices, mfg = self.sample_mfg(graph, node_indices, replace=True)
            self.MFGs.append(mfg)

        return self.MFGs
