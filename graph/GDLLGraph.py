import os
import pandas as pd
import networkx as nx
class Graph:
    def loadGraph(data_dir, dataset, sep):
        # Load Data
        print("Loading Data...")
        data_dir = os.path.expanduser(data_dir)
        edgelist = pd.read_csv(os.path.join(data_dir, dataset), sep= sep, header=None, names=["target", "source"])
        edgelist["target"] = edgelist["target"].astype(str)
        edgelist["source"] = edgelist["source"].astype(str)
        if len(edgelist.columns) < 2:
            print("Error: Make sure that you have given the right column separator and your data has source nodes and column nodes columns!!")
            quit()
        return nx.from_pandas_edgelist(edgelist)
