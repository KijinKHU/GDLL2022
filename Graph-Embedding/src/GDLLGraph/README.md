# GDLL Graph
### Introduction
This tutorial will help you to start with GDLL Graph.

## Getting Started

### Usage and Tutorial

#### Import GDLLGraph

```
from GDLLGraph import DGLLGraph
```

#### Set paths to the graph data



```
data_dir = "pata to DIR"
graph_structure = "path to graph structure"
graph_features = "path to graph features"
sep = "column separator"
```

#### Load Graph
```
g = GDLLGraph(data_dir, graph_structure, graph_features, sep)

```
#### Add Node
```
node = 4
g.addNode(node)
```


#### Add Edge
```
e = (1, 2)
g.addEdge(e)
```


#### Get number of nodes
```
g.getNumberOfNodes()
```

#### Remove Edge
```
e = (1, 2)
g.removeEdge(e)
```

#### Remove Node
```
node = 4
g.removeNode(node)
```

#### Get Nodes
```
g.getNodes()
```

#### Get Neighbors of a node
```
node = 3
g.GetNeighbors(node)
```




<p align="right">(<a href="#top">back to top</a>)</p>

<!-- ACKNOWLEDGMENTS -->
## Acknowledgments
* [Data and Knowlege Engineering Lab (DKE)](http://dke.khu.ac.kr/)
<p align="right">(<a href="#top">back to top</a>)</p>

## Links
Project's GitHub Link: [@Graph-Embedding](https://github.com/sahibzada-irfanullah/Graph-Embedding)

Project's PyPI Link: [@dgllge](https://pypi.org/project/dgllge/)

<p align="right">(<a href="#top">back to top</a>)</p>