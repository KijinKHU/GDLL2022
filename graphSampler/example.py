from GDLLGraph.GDLLGraph import *
import time
import argparse
import numpy as np
import math
import tqdm
import networkx

import torch
import torch.nn.functional as F
import torch.optim as optim

from GNN.utils.utils import load_data, load_khop, accuracy
from GNN.gnn.Convolution import gcnConv

import samplers

data_dir = "../Graph-Embedding/src/data/cora"
dataset = "cora.cites"
dataset_features = "cora.content"
g = GDLLGraph(data_dir, dataset, dataset_features, sep = "\t" )
# print("node_data: ",g.node_data)
# neiborsOfOneNode = g.GetNeighbors(31336)
# print(type(neiborsOfOneNode))

print([e for e in g.getNodes(31336)])
print([e for e in g.getEdges(('31336'))])



fanouts = "10,10,10"

# sampler = samplers.NeighborSamplerByNode(g, fanouts, edge_dir="in")
