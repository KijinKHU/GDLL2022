import time
import datetime
from datetime import datetime
import sys
sys.path.append("..")
from utils import saveEmbedding
from deepWalk import DeepWalk
from node2vec import Node2vec
from struc2vec import Struc2Vec
from GDLLGraph import GDLLGraph
from skipgram import SkipGramModel



# Set Path to data
# dataset = "citeseer - Copy"
graph = "cora.cites"
graph_features = "cora.content"

# set path to data
# graph = "cora - copy1.cites"
# graph_features = "cora - copy1.content"

data_dir = "../data/cora"
embedding_dir = "../data"
script_start = datetime.now()
# Load graph

myGraph = GDLLGraph(data_dir, graph, graph_features = graph_features, sep= "\t").g

# Set Parameters
embedDim = 128 # embedding size
numbOfWalksPerVertex = 30 # walks per vertex
walkLength = 40# walk length
lr =0.25 # learning rate
windowSize = 10 # window size

# Choose of the following embedding model
# DeepWalk
# deepwalk_start = datetime.now()
# rw = DeepWalk(myGraph, walkLength=walkLength, embedDim=embedDim, numbOfWalksPerVertex=numbOfWalksPerVertex, \
#                  windowSize=windowSize, lr = lr)
# deepwalk_end = datetime.now() - deepwalk_start
# print("\nTotal time for random walk completion :", deepwalk_end.microseconds)
#

#  Node2Vec
# n2v_start = datetime.now()
# rw = Node2vec(myGraph, walkLength=walkLength, embedDim=embedDim, numbOfWalksPerVertex=numbOfWalksPerVertex, \
#                  windowSize=windowSize, lr=lr, p = 0.5, q = 1)
# n2v_end = datetime.now() - n2v_start
# print("\nTotal time for node2vec completion :", n2v_end.microseconds)
#
#  Struc2Vec
s2v_start = datetime.now()
rw = Struc2Vec(myGraph, walkLength=walkLength, embedDim=embedDim, numbOfWalksPerVertex=numbOfWalksPerVertex, \
                  windowSize=windowSize, lr = lr, stay_prob=0.3)
s2v_end = datetime.now() - s2v_start
print("\nTotal time for struc2vec completion :", s2v_end.microseconds)
#
#
# # Skip Gram model

model_skip_gram = SkipGramModel(rw.totalNodes, rw.embedDim)

#
print("Learning Embedding")
# Learning Node Embedding
skipGram_start = datetime.now()
model = rw.learnNodeEmbedding(model_skip_gram)
skipGram_end = datetime.now() - skipGram_start
print("\nTotal time for skipgram completion :" + str(skipGram_end.microseconds))

# Learning Edge Embedding
# model = dw.learnEdgeEmbedding(model_skip_gram)


# Save embedding to disk
print("Saving embedding to disk")
saveEmbedding(data_dir, graph_features, embedding_dir, rw)
#
# print("Generating embedding for a node and edge")
# node1 = 35
# node2 = 36
# # Get Embedding for a node
# emb = rw.getNodeEmbedding(node1)
# print("Node Embedding", emb.data)
# #
# # # Get Embedding for an edge
# emb = rw.getEdgeEmbedding(node1, node2)
# print("Edge Embedding", emb.data)
Total_time = datetime.now() - script_start
print("\nTotal time for script completion :", Total_time.microseconds)




