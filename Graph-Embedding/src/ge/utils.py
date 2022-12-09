import pandas as pd
import numpy as np
import math
from fastdtw import fastdtw
import os
import networkx as nx
import numpy as np
import scipy.sparse as sp

from sklearn.metrics import classification_report
from sklearn.metrics import accuracy_score
import warnings
import csv
import pickle
warnings.filterwarnings("ignore")

def chooseNeighbor(v, graphs, layers_alias, layers_accept, layer):

    v_list = graphs[layer][v]

    idx = alias_sample(layers_accept[layer][v], layers_alias[layer][v])
    v = v_list[idx]

    return v

def alias_sample(accept, alias):
    """
    :param accept:
    :param alias:
    :return: sample index
    """
    N = len(accept)
    i = int(np.random.random()*N)
    r = np.random.random()
    if r < accept[i]:
        return i
    else:
        return alias[i]

def preprocess_nxgraph(graph):
    node2idx = {}
    idx2node = []
    node_size = 0
    for node in graph.nodes():
        node2idx[node] = node_size
        idx2node.append(node)
        node_size += 1
    return idx2node, node2idx

def partition_dict(vertices, workers):
    batch_size = (len(vertices) - 1) // workers + 1
    part_list = []
    part = []
    count = 0
    for v1, nbs in vertices.items():
        part.append((v1, nbs))
        count += 1
        if count % batch_size == 0:
            part_list.append(part)
            part = []
    if len(part) > 0:
        part_list.append(part)
    return part_list

def create_alias_table(area_ratio):
    l = len(area_ratio)
    accept, alias = [0] * l, [0] * l
    small, large = [], []
    area_ratio_ = np.array(area_ratio) * l
    for i, prob in enumerate(area_ratio_):
        if prob < 1.0:
            small.append(i)
        else:
            large.append(i)

    while small and large:
        small_idx, large_idx = small.pop(), large.pop()
        accept[small_idx] = area_ratio_[small_idx]
        alias[small_idx] = large_idx
        area_ratio_[large_idx] = area_ratio_[large_idx] - \
                                 (1 - area_ratio_[small_idx])
        if area_ratio_[large_idx] < 1.0:
            small.append(large_idx)
        else:
            large.append(large_idx)

    while large:
        large_idx = large.pop()
        accept[large_idx] = 1
    while small:
        small_idx = small.pop()
        accept[small_idx] = 1

    return accept, alias


def cost(a, b):
    ep = 0.5
    m = max(a, b) + ep
    mi = min(a, b) + ep
    return ((m / mi) - 1)


def cost_min(a, b):
    ep = 0.5
    m = max(a[0], b[0]) + ep
    mi = min(a[0], b[0]) + ep
    return ((m / mi) - 1) * min(a[1], b[1])


def cost_max(a, b):
    ep = 0.5
    m = max(a[0], b[0]) + ep
    mi = min(a[0], b[0]) + ep
    return ((m / mi) - 1) * max(a[1], b[1])


def convert_dtw_struc_dist(distances, startLayer=1):
    for vertices, layers in distances.items():
        keys_layers = sorted(layers.keys())
        startLayer = min(len(keys_layers), startLayer)
        for layer in range(0, startLayer):
            keys_layers.pop(0)

        for layer in keys_layers:
            layers[layer] += layers[layer - 1]
    return distances


def get_vertices(v, degree_v, degrees, n_nodes):
    a_vertices_selected = 2 * math.log(n_nodes, 2)
    vertices = []
    try:
        c_v = 0

        for v2 in degrees[degree_v]['vertices']:
            if (v != v2):
                vertices.append(v2)  # same degree
                c_v += 1
                if (c_v > a_vertices_selected):
                    raise StopIteration

        if ('before' not in degrees[degree_v]):
            degree_b = -1
        else:
            degree_b = degrees[degree_v]['before']
        if ('after' not in degrees[degree_v]):
            degree_a = -1
        else:
            degree_a = degrees[degree_v]['after']
        if (degree_b == -1 and degree_a == -1):
            raise StopIteration  # not anymore v
        degree_now = verifyDegrees(degrees, degree_v, degree_a, degree_b)
        # nearest valid degree
        while True:
            for v2 in degrees[degree_now]['vertices']:
                if (v != v2):
                    vertices.append(v2)
                    c_v += 1
                    if (c_v > a_vertices_selected):
                        raise StopIteration

            if (degree_now == degree_b):
                if ('before' not in degrees[degree_b]):
                    degree_b = -1
                else:
                    degree_b = degrees[degree_b]['before']
            else:
                if ('after' not in degrees[degree_a]):
                    degree_a = -1
                else:
                    degree_a = degrees[degree_a]['after']

            if (degree_b == -1 and degree_a == -1):
                raise StopIteration

            degree_now = verifyDegrees(degrees, degree_v, degree_a, degree_b)

    except StopIteration:
        return list(vertices)

    return list(vertices)


def verifyDegrees(degrees, degree_v_root, degree_a, degree_b):

    if(degree_b == -1):
        degree_now = degree_a
    elif(degree_a == -1):
        degree_now = degree_b
    elif(abs(degree_b - degree_v_root) < abs(degree_a - degree_v_root)):
        degree_now = degree_b
    else:
        degree_now = degree_a

    return degree_now

def compute_dtw_dist(part_list, degreeList, dist_func):
    dtw_dist = {}
    for v1, nbs in part_list:
        lists_v1 = degreeList[v1]  # lists_v1 :orderd degree list of v1
        for v2 in nbs:
            lists_v2 = degreeList[v2]  # lists_v1 :orderd degree list of v2
            max_layer = min(len(lists_v1), len(lists_v2))  # valid layer
            dtw_dist[v1, v2] = {}
            for layer in range(0, max_layer):
                dist, path = fastdtw(
                    lists_v1[layer], lists_v2[layer], radius=1, dist=dist_func)
                dtw_dist[v1, v2][layer] = dist
    return dtw_dist

def operator_hadamard(u, v):
    return u * v





# Save embedding to disk
def saveEmbedding(data_dir, dataset, embedding_dir, dw):

    try:

        # if content file is given then code in the "try" section will run
        idsFeaturesLabels = np.genfromtxt("{}{}".format(data_dir + "/", dataset), dtype=np.dtype(str))


        embCol = list()
        for i in range(dw.embedDim):
            embCol.append('c' + str(i))
        # append label
        embCol.append('class_label')
        idsLabels = idsFeaturesLabels[:, [0, -1]]
        df = pd.DataFrame(columns = embCol,
                          index = list(idsLabels[:,0])) # adding nodes as an index

        for row in idsLabels:

            # Get embedding for a node

            f = dw.getNodeEmbedding(row[0]).tolist()
            # append label
            f.append(row[1])
            df.loc[row[0]] = f


        df.to_csv("{}{}.embedding".format(embedding_dir + "/", dataset), sep='\t', header=False)

    except:
        # if content file is not given then code in the "except" section will run

        idsFeaturesLabels = np.array(list(dw.graph.nodes()))

        embCol = list()
        for i in range(dw.embedDim):
            embCol.append('c' + str(i))

        ids = idsFeaturesLabels
        df = pd.DataFrame(columns = embCol,
                          index = list(ids)) # adding nodes as an index

        for row in ids:
            # Get embedding for a node
            f = dw.getNodeEmbedding(row).tolist()

            df.loc[row] = f
        df.to_csv("{}{}.embedding".format(embedding_dir + "/", dataset), sep='\t', header=False)


def loadGraph(data_dir, dataset, sep):
    # Load Data
    print("Loading Data...")
    data_dir = os.path.expanduser(data_dir)
    edgelist = pd.read_csv(os.path.join(data_dir, dataset), sep= sep, header=None, names=["target", "source"])
    # edgelist["target"] = edgelist["target"].values.astype(str)
    # edgelist["source"] = edgelist["source"].values.astype(str)
    edgelist["target"] = edgelist["target"].astype(str)
    edgelist["source"] = edgelist["source"].astype(str)
    if len(edgelist.columns) < 2:
        print("Error: Make sure that you have given the right column separator and your data has source nodes and column nodes columns!!")
        quit()
    return nx.from_pandas_edgelist(edgelist)

# custom format warning with only a message
def custom_formatwarning(msg, *args, **kwargs):
    return str(msg) + '\n'
warnings.formatwarning = custom_formatwarning

def loadEmbeddingWithClassLabels(path):
    print("Load Embedding")
    idsEmbeddingClsLabels = np.genfromtxt(path, dtype=np.dtype(str))
    labels = idsEmbeddingClsLabels[:, -1]
    embedding = sp.csr_matrix(idsEmbeddingClsLabels[:, 1:-1], dtype=np.float32)
    return embedding, labels

def loadEmbedding(path):
    print("Load Embedding")
    idsEmbedding = np.genfromtxt(path, dtype=np.dtype(str))
    embedding = sp.csr_matrix(idsEmbedding[:, 1:], dtype=np.float32)
    return embedding
def train_test_split(embedding, labels, test_split = 0.33):
    from Classifiers import TrainingClassifiers
    tr = TrainingClassifiers()
    X_train, X_test, y_train, y_test = tr.prepareTrainTestData(embedding, labels, test_split)
    return X_train, X_test, y_train, y_test

def classify(X_train, y_train, X_test, classifier = "MLP"):
    from Classifiers import TrainingClassifiers
    tr = TrainingClassifiers()
    if classifier.lower() in ["decision tree", "dt"]:
        y_pred, clf = tr.applyDecisionTree(X_train, y_train, X_test)
    elif classifier.lower() in ["logistic regression", "lr"]:
        y_pred, clf = tr.applyLogistic(X_train, y_train, X_test)
    elif classifier.lower() in ["random forest", "rf"]:
        y_pred, clf = tr.applyRandomForest(X_train, y_train, X_test)
    elif classifier.lower() in ["gradient boosting", "gb"]:
        y_pred, clf = tr.applyGradientBoosting(X_train, y_train, X_test)
    elif classifier.lower() in ["multilayer preceptron", "mlp"]:
        y_pred, clf = tr.applyMLP(X_train, y_train, X_test)
    return y_pred, clf

def accuracy(y_true, y_pred):
    return accuracy_score(y_true, y_pred)




