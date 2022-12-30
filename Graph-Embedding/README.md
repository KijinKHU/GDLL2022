# Graph Embedding
### Introduction
This module provides the services and implementation for various graph embedding models.

## Getting Started

## Usage and Tutorial
#### input graph
```
from utils import loadGraph, saveEmbedding
from deepWalk import DeepWalk
from node2vec import Node2vec
from skipgram import SkipGramModel

# Set Path to Data
data_dir = "Your Path to Data"
dataset = "File/Dataset Name"
sep = "column separator"


# Load a Graph
inputGraph = ge.loadGraph(data_dir, dataset, sep)
```

#### Configurable Parameter for Graph Embedding
```
embedDim = 2 # embedding size
numbOfWalksPerVertex = 2 # walks per vertex
walkLength = 4 # walk lenght
lr =0.025 # learning rate
windowSize = 3 # window size
```

#### Choose One of the Following Graph Embedding Models
```
# DeepWalk
rw = DeepWalk(inputGraph, walkLength=walkLength, embedDim=embedDim, numbOfWalksPerVertex=numbOfWalksPerVertex, \
              windowSize=windowSize, lr = lr)
              
 ```
```
# Node2Vec
rw = Node2vec(inputGraph, walkLength=walkLength, embedDim=embedDim, numbOfWalksPerVertex=numbOfWalksPerVertex, \
               windowSize=windowSize, lr=lr, p = 0.5, q = 0.8)
```
```
# Struc2Vec
rw = Struc2Vec(inputGraph, walkLength=walkLength, embedDim=embedDim, numbOfWalksPerVertex=numbOfWalksPerVertex, \
              windowSize=windowSize, lr = lr)
```
              
#### Skip Gram model
```
modelSkipGram = SkipGramModel(rw.totalNodes, rw.embedDim)
```
#### Want Node Embedding or Edge Embedding
```
# Learning Node Embedding
model = rw.learnNodeEmbedding(modelSkipGram)
```


```
# Learning Edge Embedding
model = rw.learnEdgeEmbedding(modelSkipGram)
```

#### Save Embedding to Disk
```
saveEmbedding(data_dir, dataset, rw)
```
#### Generate  Embedding for a Specific Node or Edge
```
node1 = 35
node2 = 40

# Get Embedding for a node
emb = rw.getNodeEmbedding(node1)
print("Node Embedding", emb)

# Get Embedding for an edge
emb = rw.getEdgeEmbedding(node1, node2)
print("Edge Embedding", emb)
```

### Utilize embedding for training classification models/classifiers

```
from utils import loadEmbeddingWithClassLabels, train_test_split, classify, accuracy
```

#### Load Embedding
```
embedding, labels = loadEmbeddingWithClassLabels("Name of the embedding file")
```


#### Prepare Train test data
```
X_train, X_test, y_train, y_test = ge.train_test_split(embedding, labels, test_split = 0.33)
```

#### Choose one of the following classifier for training a classifier

```
y_pred, clf = classify(X_train, y_train, X_test, classifier = "gb")
```
Here, classifier = ["decision tree", "dt", "logistic regression", "lr", "random forest", "rf" ,
"gradient boosting", "gb", "multilayer preceptron", "mlp"]
#### Get Accuracy
```
print("Accuracy:", accuracy(y_test, y_pred))
```


#### How to run
##### For learning embedding:
```
python main_ge.py
```
##### For learning classification:
```
python main_trainClf.py
```
<!-- LICENSE.txt -->
## License

Distributed under the MIT License. See `LICENSE` for more information.

<p align="right">(<a href="#top">back to top</a>)</p>

<!-- ACKNOWLEDGMENTS -->
## Acknowledgments
* [Data and Knowlege Engineering Lab (DKE)](http://dke.khu.ac.kr/)
<p align="right">(<a href="#top">back to top</a>)</p>

## Links
Project's GitHub Link: [@Graph-Embedding](https://github.com/sahibzada-irfanullah/Graph-Embedding)

Project's PyPI Link: [@dgllge](https://pypi.org/project/dgllge/)

<p align="right">(<a href="#top">back to top</a>)</p>