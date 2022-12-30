# Graph Sampler

## Load Graph data

g = GDLLGraph(data_dir, dataset, dataset_features, sep = "\t" )
print(g.node_data)

## Set example settings
fanouts = "10,10,10"
exmaple_seeds = [12134, 11223] 

sampler = samplers.NeighborSamplerByNode(g, fanouts, edge_dir="in")

src_nodes=[]

dst_nodes=[]

MFG = None

[src_nodes, dst_nodes, MFG] = sampler.sample(g, exmaple_seeds)

