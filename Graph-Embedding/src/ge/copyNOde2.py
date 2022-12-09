import torch
import numpy as np
from collections import defaultdict
import warnings
import sys
import pickle
import csv
from csv import reader
from sklearn import preprocessing
import networkx as nx
try:
    from utils import operator_hadamard, custom_formatwarning
    from randomWalkEmbedding import RandomWalkEmbedding
except ModuleNotFoundError:
    from .utils import operator_hadamard, custom_formatwarning
    from .randomWalkEmbedding import RandomWalkEmbedding

class Node2vec(RandomWalkEmbedding):
    # Constructor
    def __init__(self, graph = None, walkLength = 0, embedDim = 0, numbOfWalksPerVertex = 0, \
                 windowSize = 0, lr = 0, p = 0, q = 0):
        if graph is None:
            warnings.warn("Provide a graph: {}".format(graph))
            sys.exit()
        super(Node2vec, self).__init__(graph, walkLength, embedDim, numbOfWalksPerVertex,  windowSize, lr)

        # validate arguments
        if p == 0:
            self.p = 0.5
            warnings.warn("Set p to default: {}".format(self.p))
        else:
            self.p = p
        if q == 0:
            self.q = 0.8
            warnings.warn("Set q to default: {}".format(self.q))
        else:
            self.q = q
        self.model = None
        self.nodeEncoder = self.encoder(graph)
    def encoder(self, graph):

        nodeEncoder = preprocessing.LabelEncoder()
        # print(str(list(graph.nodes())))
        # nodes_list = list(map(str, list(graph.nodes())))
        # nodeEncoder.fit(list(graph.nodes()))
        # nodeEncoder.fit(list(map(str, list(graph.nodes()))))
        return nodeEncoder.fit(list(graph.nodes()))

    # Calculating Probabilities for traversing a node
    def computeProbabilities(self, source_node):
        print("current node", source_node)
        if self.graph.has_node(str(source_node)):
            source_node = str(source_node)
        else:
            source_node = int(source_node)
        probs = defaultdict(dict)
        probs[source_node]['probabilities'] = dict()

        for current_node in self.graph.neighbors(source_node):
            probs_ = list()
            for destination in self.graph.neighbors(current_node):
                if source_node == destination:
                    prob_ = self.graph[current_node][destination].get('weight',1) * (1/self.p)
                elif destination in self.graph.neighbors(str(source_node)):
                    prob_ = self.graph[current_node][destination].get('weight',1)
                else:
                    prob_ = self.graph[current_node][destination].get('weight',1) * (1/self.q)
                probs_.append(prob_)
            probs[source_node]['probabilities'][current_node] = probs_/np.sum(probs_)
        return probs

    # Walks generation
    def RandomWalk(self):
        f = open('../data/struc2vec_input.csv', 'w', newline ='')
        for startNode in list(self.graph.nodes):
            for i in range(self.numbOfWalksPerVertex):
                # walkStartNode = self.RandomWalk(startNode, self.walkLength)
                # self.model = self.learnEmbedding(self.model, walkStartNode)
                # walk contains encoded node labels
                walk = [int(self.nodeEncoder.transform([startNode]))]
                walkOptions = list(self.graph[startNode])
                if len(walkOptions)==0:
                    return walk
                firstStep = np.random.choice(walkOptions)
                print(self.nodeEncoder.transform([str(firstStep)])[0])
                walk.append(int(self.nodeEncoder.transform([str(firstStep)])[0]))
                for k in range(self.walkLength-2):
                    if self.graph.has_node(str(self.nodeEncoder.inverse_transform([walk[-1]])[0])):
                        walkOptions = list(self.graph[str(self.nodeEncoder.inverse_transform([walk[-1]])[0])])
                    if self.graph.has_node(int(self.nodeEncoder.inverse_transform([walk[-1]])[0])):
                        walkOptions = list(self.graph[int(self.nodeEncoder.inverse_transform([walk[-1]])[0])])
                    if len(walkOptions)==0:
                        break
                    probs = self.computeProbabilities((self.nodeEncoder.inverse_transform([walk[-2]])[0]))
                    probabilities = probs[(self.nodeEncoder.inverse_transform([walk[-2]])[0])] \
                        ['probabilities'][(self.nodeEncoder.inverse_transform([walk[-1]])[0])]
                    # Traverse a vertex from a neighbors of node 'node"
                    nextStep = np.random.choice(walkOptions, p=probabilities)
                    walk.append(int(self.nodeEncoder.transform([nextStep])))

                write = csv.writer(f)
                write.writerow(walk)
        pkl_file = open('../data/node2vec_encoder.pkl', 'wb')
        pickle.dump(self.nodeEncoder, pkl_file)
        pkl_file.close()
        # return walk

    # Generate features for nodes
    def generateNodeFeatures(self, totalNodes, wvi, j):
        nodeFeatures = torch.zeros(totalNodes)
        nodeFeatures[int(wvi[j])] = 1
        return nodeFeatures

    # Training graph embedding model
    # Training graph embedding model
    def learnEmbedding(self):
        f = open('../data/node2vec_input.csv', 'r')
        file_reader = reader(f)
        for wvi in file_reader:
            for j in range(len(wvi)):
                for k in range(max(0,j-self.windowSize) , min(j+self.windowSize, len(wvi))):
                    # generate features
                    nodeFeatures = self.generateNodeFeatures(self.totalNodes, wvi, j)
                    out = self.model.forward(nodeFeatures)
                    loss = torch.log(torch.sum(torch.exp(out))) - out[int(wvi[k])]
                    loss.backward()
                    for param in self.model.parameters():
                        param.data.sub_(self.lr*param.grad)
                        param.grad.data.zero_()
        return self.model

    # Get node embedding for a specific node, i.e., "node"
    def getNodeEmbedding(self, node):
        return self.model.W1[int(self.nodeEncoder.transform([node]))].data

    # Training node embedding model
    def learnNodeEmbedding(self, model):
        self.model = model
        self.RandomWalk()
        self.model = self.learnEmbedding()
        # for startNode in list(self.graph.nodes):
        #     for i in range(self.numbOfWalksPerVertex):
        #         walkStartNode = self.RandomWalk(startNode, self.walkLength)
        #         self.model = self.learnEmbedding(self.model, walkStartNode)
        return self.model

    # Training edge embedding model
    def learnEdgeEmbedding(self, model):
        self.model = model
        for startNode in list(self.graph.nodes):
            for i in range(self.numbOfWalksPerVertex):
                walkStartNode = self.RandomWalk(startNode, self.walkLength)
                self.model = self.learnEmbedding(self.model, walkStartNode)
        return self.model

    # Get edge embedding for a specific edge having source node, i.e., "srcNode" and destination node, i.e., dstNode
    def getEdgeEmbedding(self, srcNode, dstNode):
        # Operator_hadamrd defined in Utils
        return operator_hadamard(self.getNodeEmbedding(srcNode), self.getNodeEmbedding(dstNode))



