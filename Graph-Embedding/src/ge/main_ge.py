
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
# dataset = "cora.cites"
# dataset_class = "cora.content"

# set path to data
graph = "cora - copy1.cites"
graph_features = "cora - copy1.content"

data_dir = "../data/cora"
embedding_dir = "../data"

# Load graph

myGraph = GDLLGraph(data_dir, graph, graph_features = graph_features, sep= "\t").g

# Set Parameters
embedDim = 5 # embedding size
numbOfWalksPerVertex = 2 # walks per vertex
walkLength = 3# walk length
lr =0.25 # learning rate
windowSize = 3 # window size

# Choose of the following embedding model
# DeepWalk
# rw = DeepWalk(myGraph, walkLength=walkLength, embedDim=embedDim, numbOfWalksPerVertex=numbOfWalksPerVertex, \
#                  windowSize=windowSize, lr = lr)
#
# #  Node2Vec
# rw = Node2vec(myGraph, walkLength=walkLength, embedDim=embedDim, numbOfWalksPerVertex=numbOfWalksPerVertex, \
#                  windowSize=windowSize, lr=lr, p = 0.5, q = 1)
#
#  Struc2Vec
rw = Struc2Vec(myGraph, walkLength=walkLength, embedDim=embedDim, numbOfWalksPerVertex=numbOfWalksPerVertex, \
                  windowSize=windowSize, lr = lr, stay_prob=0.3)
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




