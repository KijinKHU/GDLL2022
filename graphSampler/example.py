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



# sampled MFGs result
# [
# [['31336', '31336', '31336', '1129442', '1129442', '1129442'],
# ['31353', '686532', '686532', '31353', '43698', '31353'],
# <networkx.classes.graph.Graph object at 0x0000022E9DB09310>],
#
# [['31353', '31353', '31353', '686532', '686532', '686532', '686532', '686532',
# '686532', '31353', '31353', '31353', '43698', '43698', '43698', '31353', '31353', '31353'],
# ['31336', '1124844', '1123576', '31349', '31353', '43698', '31353', '31353',
# '31349', '194617', '1063773', '31349', '31349', '10531', '31349', '1129442', '686532', '286562'],
# <networkx.classes.graph.Graph object at 0x0000022E9DB09370>]
# ]



fanouts = "10,10,10"

# sampler = samplers.NeighborSamplerByNode(g, fanouts, edge_dir="in")
