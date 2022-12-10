"""Base classes and functionalities for dataloaders"""
from collections.abc import Mapping

class LazyFeature(object):

    __slots__ = ["name", "id_"]

    def __init__(self, name=None, id_=None):
        self.name = name
        self.id_ = id_

    def to(
        self, *args, **kwargs
    ):  # pylint: disable=invalid-name, unused-argument
        """No-op.  For compatibility of :meth:`Frame.to` method."""
        return self

    @property
    def data(self):
        """No-op.  For compatibility of :meth:`Frame.__repr__` method."""
        return self

    def pin_memory_(self):
        """No-op.  For compatibility of :meth:`Frame.pin_memory_` method."""

    def unpin_memory_(self):
        """No-op.  For compatibility of :meth:`Frame.unpin_memory_` method."""

    def record_stream(self, stream):
        """No-op.  For compatibility of :meth:`Frame.record_stream` method."""


def _set_lazy_features(x, xdata, feature_names):
    if feature_names is None:
        return
    if not isinstance(feature_names, Mapping):
        xdata.update({k: LazyFeature(k) for k in feature_names})
    else:
        for type_, names in feature_names.items():
            x[type_].data.update({k: LazyFeature(k) for k in names})

def set_node_lazy_features(g, feature_names):
    """
    Parameters
    ----------
    g : The graph.
    feature_names : list[str] or dict[str, list[str]]
        The feature names to prefetch.
    See also
    --------
    LazyFeature
    """
    return _set_lazy_features(g.nodes, g.ndata, feature_names)

def set_edge_lazy_features(g, feature_names):
    """
    Parameters
    ----------
    g : DGLGraph
        The graph.
    feature_names : list[str] or dict[etype, list[str]]
        The feature names to prefetch. The ``etype`` key is either a string
        or a triplet.
    See also
    --------
    LazyFeature
    """
    return _set_lazy_features(g.edges, g.edata, feature_names)

def set_src_lazy_features(g, feature_names):
    """
    Parameters
    ----------
    g : The graph.
    feature_names : list[str] or dict[str, list[str]]
        The feature names to prefetch.
    See also
    --------
    LazyFeature
    """
    return _set_lazy_features(g.srcnodes, g.srcdata, feature_names)

def set_dst_lazy_features(g, feature_names):
    """
    Parameters
    ----------
    g : The graph.
    feature_names : list[str] or dict[str, list[str]]
        The feature names to prefetch.
    See also
    --------
    LazyFeature
    """
    return _set_lazy_features(g.dstnodes, g.dstdata, feature_names)

class Sampler(object):

    def sample(self, g, indices):
       raise NotImplementedError


class BlockSampler(Sampler):

    def __init__(self, prefetch_node_feats=None, prefetch_labels=None,
                 prefetch_edge_feats=None, output_device=None):
        super().__init__()
        self.prefetch_node_feats = prefetch_node_feats or []
        self.prefetch_labels = prefetch_labels or []
        self.prefetch_edge_feats = prefetch_edge_feats or []
        self.output_device = output_device

    def sample_blocks(self, g, seed_nodes, exclude_eids=None):
        """Generates a list of blocks from the given seed nodes.
        This function must return a triplet where the first element is the input node IDs
        for the first GNN layer (a tensor or a dict of tensors for heterogeneous graphs),
        the second element is the output node IDs for the last GNN layer, and the third
        element is the said list of blocks.
        """
        raise NotImplementedError

    def assign_lazy_features(self, result):
        """Assign lazy features for prefetching."""
        input_nodes, output_nodes, blocks = result
        set_src_lazy_features(blocks[0], self.prefetch_node_feats)
        set_dst_lazy_features(blocks[-1], self.prefetch_labels)
        for block in blocks:
            set_edge_lazy_features(block, self.prefetch_edge_feats)
        return input_nodes, output_nodes, blocks

    def sample(self, g, seed_nodes, exclude_eids=None):     # pylint: disable=arguments-differ
        """Sample a list of blocks from the given seed nodes."""
        result = self.sample_blocks(g, seed_nodes)
        return self.assign_lazy_features(result)
