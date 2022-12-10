# import module

import time
import datetime
from utils import loadGraph, saveEmbedding
from deepWalk import DeepWalk
from node2vec import Node2vec
from skipgram import SkipGramModel


script_start = time.time()
# Set Path to data
# dataset = "citeseer - Copy"
# dataset = "cora.cites"
dataset = "cora - copy1.cites"
dataset_class = "cora - copy1.content"

data_dir = "../data/cora"
embedding_dir = "../data"

# input graph


myGraph = loadGraph(data_dir, dataset, sep = "\t")

print(myGraph)
#
# Set Parameters
embedDim = 3 # embedding size
numbOfWalksPerVertex = 3 # walks per vertex
walkLength = 3# walk length
lr =0.25 # learning rate
windowSize = 3 # window size

# Choose of the following embedding model
# DeepWalk
rw = DeepWalk(myGraph, walkLength=walkLength, embedDim=embedDim, numbOfWalksPerVertex=numbOfWalksPerVertex, \
                 windowSize=windowSize, lr = lr)
#
# #  Node2Vec
# rw = ge.Node2vec(myGraph, walkLength=walkLength, embedDim=embedDim, numbOfWalksPerVertex=numbOfWalksPerVertex, \
#                  windowSize=windowSize, lr=lr, p = 0.5, q = 1)
#
# # # # Struc2Vec
# # rw = ge.Struc2Vec(myGraph, walkLength=walkLength, embedDim=embedDim, numbOfWalksPerVertex=numbOfWalksPerVertex, \
# #                   windowSize=windowSize, lr = lr, stay_prob=0.3)
#
#
# # Skip Gram model
model_skip_gram = SkipGramModel(rw.totalNodes, rw.embedDim)
#
print("Learning Embedding")
# Learning Node Embedding
model = rw.learnNodeEmbedding(model_skip_gram)

# Learning Edge Embedding
# model = dw.learnEdgeEmbedding(model_skip_gram)


# Save embedding to disk
print("Saving embedding to disk")
saveEmbedding(data_dir, dataset_class, embedding_dir, rw)
#
# print("Generating embedding for a node and edge")
node1 = 35
node2 = 36
# Get Embedding for a node
emb = rw.getNodeEmbedding(node1)
print("Node Embedding", emb.data)
# #
# # # Get Embedding for an edge
# emb = rw.getEdgeEmbedding(node1, node2)
# print("Edge Embedding", emb.data)
Total_time = time.time() - script_start
print("\nTotal time for script completion :" + str(datetime.timedelta(seconds=int(Total_time))))



