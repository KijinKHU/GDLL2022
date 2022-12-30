# Graph Sampler

g = GDLLGraph(data_dir, dataset, dataset_features, sep = "\t" )
print(g.node_data)

fanouts = "10,10,10"

sampler = samplers.NeighborSamplerByNode(g, fanouts, edge_dir="in")

[src_nodes, dst_nodes, MFG] = sampler