import torch
import random
import warnings
import sys
import pickle
import csv
from csv import reader
try:
    from utils import operator_hadamard, custom_formatwarning
    from randomWalkEmbedding import RandomWalkEmbedding
except ModuleNotFoundError:
    from .utils import operator_hadamard, custom_formatwarning
    from .randomWalkEmbedding import RandomWalkEmbedding

class DeepWalk(RandomWalkEmbedding):
    # constructor
    def __init__(self, graph = None, walkLength = 0, embedDim = 0, numbOfWalksPerVertex = 0, \
                 windowSize = 0, lr = 0):
        if graph is None:
            warnings.warn("Provide a graph: {}".format(graph))
            sys.exit()
        super(DeepWalk, self).__init__(graph, walkLength, embedDim, numbOfWalksPerVertex,  windowSize, lr)
        self.model = None



    # Generate features for nodes
    def generateNodeFeatures(self, totalNodes, wvi, j):
        nodeFeatures = torch.zeros(totalNodes)

        nodeFeatures[int(wvi[j])] = 1
        return nodeFeatures

    # Training graph embedding model
    def learnEmbedding(self):
        # pkl_file = open('data/deepwalk_encoder.pkl', 'rb')
        # nodeEncoder_pkl_file = pickle.load(pkl_file)
        # pkl_file.close()
        f = open('data/ML_input.csv', 'r')
        file_reader = reader(f)
        for wvi in file_reader:
            for j in range(len(wvi)):
                for k in range(max(0,j-self.windowSize) , min(j+self.windowSize, len(wvi))):
                    # Getting node for a specific features
                    nodeFeatures = self.generateNodeFeatures(self.totalNodes, wvi, j)
                    out = self.model.forward(nodeFeatures)
                    loss = torch.log(torch.sum(torch.exp(out))) - out[int(wvi[k])]
                    loss.backward()
                    for param in self.model.parameters():
                        param.data.sub_(self.lr*param.grad)
                        param.grad.data.zero_()
        return self.model

    # Walks generation
    def RandomWalk(self):

        f = open('data/ML_input.csv', 'w', newline ='')
        nodesList = list(self.graph.nodes)
        # Number of walks for a single vertex
        for i in range(self.numbOfWalksPerVertex):
            random.shuffle(nodesList)
            # Generating walk for a vertex
            for node in nodesList:
                # walk contains encoded node labels
                # walk = [int(self.nodeEncoder.transform([node]))]        # Walk starts from node "node"
                if self.graph.has_node(node):
                    try:
                        walk = [int(self.nodeEncoder.transform([node]))]
                    except:
                        walk = [int(self.nodeEncoder.transform([str(node)]))]
                elif self.graph.has_node(str(node)):
                    walk = [int(self.nodeEncoder.transform([str(node)]))]
                elif self.graph.has_node(int(node)):
                    walk = [int(self.nodeEncoder.transform([int(node)]))]
                for i in range(self.walkLength - 1):
                    neighborsList = [n for n in self.graph.neighbors(node)]
                    # if self.graph.has_node(node):
                    #     neighborsList = [n for n in self.graph.neighbors(node)]
                    # elif self.graph.has_node(str(node)):
                    #     neighborsList = [n for n in self.graph.neighbors(str(node))]
                    # elif self.graph.has_node(int(node)):
                    #     neighborsList = [n for n in self.graph.neighbors(int(node))]

                    # Randomly traverse a vertex from a neighbors of node 'node"
                    node = neighborsList[random.randint(0,len(neighborsList)-1)]
                    if self.graph.has_node(node):
                        try:
                            walk.append(int(self.nodeEncoder.transform([node])))
                        except TypeError:
                            walk.append(int(self.nodeEncoder.transform([str(node)])))

                    elif self.graph.has_node(str(node)):
                        walk.append(int(self.nodeEncoder.transform([str(node)])))
                    elif self.graph.has_node(int(node)):
                        walk.append(int(self.nodeEncoder.transform([int(node)])))
                    # walk.append(int(self.nodeEncoder.transform([node])))
                    # print(walk)
                write = csv.writer(f)
                write.writerow(walk)
        # pkl_file = open('../data/deepwalk_encoder.pkl', 'wb')
        # pickle.dump(self.nodeEncoder, pkl_file)
        # pkl_file.close()
        # return walk

    # Training node embedding model
    def learnNodeEmbedding(self, model):
        self.model = model
        self.RandomWalk()
        self.model = self.learnEmbedding()
        return self.model

    # Get node embedding for a specific node, i.e., "node"
    def getNodeEmbedding(self, node):
        try:
            emb = self.model.W1[int(self.nodeEncoder.transform([str(node)]))].data
        except ValueError:
            emb = self.model.W1[int(self.nodeEncoder.transform([int(node)]))].data
        return emb

    # Training edge embedding model
    def learnEdgeEmbedding(self, model):
        self.model = model
        self.RandomWalk()
        self.model = self.learnEmbedding()
        return self.model

    # Get edge embedding for a specific edge having source node, i.e., "srcNode" and destination node, i.e., dstNode
    def getEdgeEmbedding(self, srcNode, dstNode):
        # Operator_hadamrd defined in Utils
        return operator_hadamard(self.getNodeEmbedding(str(srcNode)), self.getNodeEmbedding(str(dstNode)))
