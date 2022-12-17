import os
import pandas as pd
import networkx as nx
class GDLLGraph:
    def __init__(self, data_dir, dataset, dataset_features = None, sep= "\t"):
        # Load Data
        print("Loading Data...")
        data_dir = os.path.expanduser(data_dir)
        edgelist = pd.read_csv(os.path.join(data_dir, dataset), sep= sep, header=None, names=["target", "source"])
        edgelist["target"] = edgelist["target"].astype(str)
        edgelist["source"] = edgelist["source"].astype(str)

        if len(edgelist.columns) < 2:
            print("Error: Make sure that you have given the right column separator and your data has source nodes and column nodes columns!!")
            quit()
        self.g = nx.from_pandas_edgelist(edgelist)

        if dataset_features != None:
            feature_names = ["w_{}".format(ii) for ii in range(1433)]
            column_names =  feature_names + ["label"]
            self.node_data = pd.read_csv(os.path.join(data_dir, dataset_features), sep='\t', header=None, names=column_names)


    def addNode(self, node):
        self.g.add_node(node)

    def getNumberOfNodes(self):
        return  self.g.number_of_nodes()

    def addEdge(self, edge):
        self.g.add_edge(edge)

    def removeEdge(self, edge):
        self.g.remove_edge(*edge)

    def getNode(self):
        return self.g.nodes

    def GetNeighbors(self, node):
        return self.g.neighbors(node)



    # def example(self):
    #     print("print sfdsf sdf")

data_dir = "../Graph-Embedding/src/data/cora"
embedding_dir = "../Graph-Embedding/src/data"
dataset = "cora - copy1.cites"
dataset_features = "cora - copy1.content"
g = Graph(data_dir, dataset, dataset_features, sep = "\t" )
print(g.node_data)