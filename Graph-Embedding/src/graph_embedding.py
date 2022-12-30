#!/usr/bin/env python
# coding: utf-8

# # Graph Embedding Demo
# This is a demo for for graph embedding utilizing DGLL graph embedding module.

# ## Get embedding for a graph

# ### Import libraries

# In[18]:


import sys
sys.path.append("..")
import warnings
warnings.filterwarnings("ignore")
from ge.utils import saveEmbedding
from ge.deepWalk import DeepWalk
from ge.node2vec import Node2vec
from ge.struc2vec import Struc2Vec
from ge.GDLLGraph import GDLLGraph
from ge.skipgram import SkipGramModel


# #### Set Path to data

# In[19]:


graph = "cora - copy1.cites"
graph_features = "cora - copy1.content"

data_dir = "data/cora"
embedding_dir = "data"


# #### Load graph
# 

# In[20]:


myGraph = GDLLGraph(data_dir, graph, graph_features = graph_features, sep= "\t").g


# #### Set Parameters

# In[21]:


embedDim = 3 # embedding size
numbOfWalksPerVertex = 3 # walks per vertex
walkLength = 3# walk length
lr =0.25 # learning rate
windowSize = 10 # window size


# #### Choose embedding approach

# In[23]:


# DeepWalk

rw = DeepWalk(myGraph, walkLength=walkLength, embedDim=embedDim, numbOfWalksPerVertex=numbOfWalksPerVertex,                  windowSize=windowSize, lr = lr)

#

#  Node2Vec

# rw = Node2vec(myGraph, walkLength=walkLength, embedDim=embedDim, numbOfWalksPerVertex=numbOfWalksPerVertex, \
#                  windowSize=windowSize, lr=lr, p = 0.5, q = 1)

#
#  Struc2Vec

# rw = Struc2Vec(myGraph, walkLength=walkLength, embedDim=embedDim, numbOfWalksPerVertex=numbOfWalksPerVertex, \
#                   windowSize=windowSize, lr = lr, stay_prob=0.3)


# #### Learning embedding
# 

# In[25]:


model_skip_gram = SkipGramModel(rw.totalNodes, rw.embedDim)
print("Learning Embedding")
model = rw.learnNodeEmbedding(model_skip_gram)


# Save embedding to disk
print("Saving embedding to disk")
saveEmbedding(data_dir, graph_features, embedding_dir, rw)


# #### Get Embedding for a node

# In[51]:


print("Generating embedding for a node")
node1 = 35
node2 = 36

# Get Embedding for a node
emb = rw.getNodeEmbedding(node1)
print("Node Embedding", emb.data)


# ## Node Classification on graph
# 

# ### Import libraries

# In[52]:


from ge.utils import loadEmbedding, train_test_split, classify, accuracy


# ### Load embedding
# This is the embedding that we generated previously.

# In[53]:


path = "../data/cora - copy1.content.embedding"
embedding, labels = loadEmbedding("data/cora - copy1.content.embedding")


# ### Prepare Train test data
# 

# In[54]:


X_train, X_test, y_train, y_test = train_test_split(embedding, labels, test_split = 0.33)


# ### learn classification

# In[55]:


y_pred, clf = classify(X_train, y_train, X_test, classifier = "lr")


# ### Choose one of the following classifier for training train a classifier

# In[56]:


print("Accuracy:", accuracy(y_test, y_pred))

