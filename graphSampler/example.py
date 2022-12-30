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
print(g.node_data)

fanouts = "10,10,10"

sampler = samplers.NeighborSamplerByNode(g, fanouts, edge_dir="in")

exmaple_seeds = [12134, 11223]
[src_nodes, dst_nodes, MFG] = sampler.sample(g, exmaple_seeds)