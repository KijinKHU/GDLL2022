from GDLLGraph.GDLLGraph import *
import time
import argparse
import numpy as np
import math
import tqdm
import networkx as nx

import torch
import torch.nn.functional as F
import torch.optim as optim

import samplers

data_dir = "../Graph-Embedding/src/data/cora"
dataset = "cora.cites"
dataset_features = "cora.content"
g = GDLLGraph(data_dir, dataset, dataset_features, sep = "\t" )
# print("node_data: ",g.node_data)
# neiborsOfOneNode = g.GetNeighbors(31336)
# print(type(neiborsOfOneNode))
fanout = [3,3]
example_seed_nodes = [('31336'),('1129442')]

sampler = samplers.NeighborSamplerByNode(fanout)
MFGs = sampler.sample(g,example_seed_nodes)
print(MFGs)



#
# [('31336', '31336'), ('31336', '31336'), ('31336', '31336')]
# print([e for e in g.getEdges(example_seed_node)])



fanouts = "10,10,10"

# sampler = samplers.NeighborSamplerByNode(g, fanouts, edge_dir="in")
