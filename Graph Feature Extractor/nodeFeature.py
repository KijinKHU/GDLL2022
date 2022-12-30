import networkx as nx
import requests
from GDLLGraph import GDLLGraph
import os
import json
from grakel import GraphKernel

# morshed,KHU
#
data_dir = ""
dataset = "cora.cites"
g = GDLLGraph(data_dir, dataset, dataset_features= None, sep = "\t" )


# Load the CORA dataset
# G = nx.read_edgelist('cora.cites')
G = nx.read_edgelist("cora.cites", create_using=nx.Graph())

print("Number of nodes:", G.number_of_nodes())
print("Number of edges:", G.number_of_edges())

# Calculate the degree of each node
degrees = nx.degree(G)

# Get the current working directory
cwd = os.getcwd()

# Construct the file path for the output file
output_file = os.path.join(cwd, 'degrees.txt')

# Write the degrees to a text file
with open(output_file, 'w') as f:
    for node, degree in degrees:
        f.write(f'{node}: {degree}\n')

print(f'Node degrees saved to {output_file}')

# Calculate betweenness centrality for each node in the graph
betweenness_centrality = nx.betweenness_centrality(G)

# Save the betweenness centrality values to a text file
with open("betweenness_centrality.txt", "w") as f:
    for node, centrality in betweenness_centrality.items():
        f.write(f"{node}: {centrality}\n")

# Calculate closeness centrality for each node in the graph
closeness_centrality = nx.closeness_centrality(G)

# Save the closeness centrality values to a text file
with open("closeness_centrality.txt", "w") as f:
    for node, centrality in closeness_centrality.items():
        f.write(f"{node}: {centrality}\n")


# Specify the source and target nodes
source = "35"
target = "1033"

# Calculate the shortest path between the source and target nodes
shortest_path = nx.shortest_path(G, source, target)

# Save the shortest path to a text file
with open("shortest_path.txt", "w") as f:
    f.write(" -> ".join(shortest_path))

# Specify the two nodes
node1 = "35"
node2 = "936"

# Calculate the common neighbors of the two nodes
common_neighbors = list(nx.common_neighbors(G, node1, node2))

# Save the common neighbors to a text file
with open("common_neighbors.txt", "w") as f:
    f.write(", ".join(common_neighbors))

# Specify the two nodes
node1 = "1129018"
node2 = "1129027"

# Calculate Jaccard's coefficient for the two nodes
jaccard_coefficient = nx.jaccard_coefficient(G, [(node1, node2)])
print(jaccard_coefficient)
# # Save the Jaccard's coefficient to a text file
with open("jaccard_coefficient.txt", "w") as f:
    f.write("Null")
#     for (n3, n4), coefficient in jaccard_coefficient:
#         f.write(f"Jaccard's coefficient between {n3} and {n4}: {coefficient}\n")

# Set the beta parameter for the Katz index
# beta = 0.1

# Calculate the Katz index for each node in the graph
# katz_index = nx.katz_centrality(G, beta=beta)
#
# Save the Katz index values to a text file
with open("katz_index.txt", "w") as f:
    f.write("Null")
#     for node, index in katz_index:
#         f.write(f"{node}: {index}\n")

# Convert the NetworkX graph to a list of grakel graphs
# grakel_graphs = [nx.Graph(G.subgraph(c)) for c in nx.connected_components(G)]
#
# # Initialize the graphlet kernel with the desired parameters
# kernel = GraphKernel(kernel="graphlet_sampling")
#
# # Calculate the graphlet kernel for each node pair
# graphlet_kernel = kernel.fit_transform(grakel_graphs)
#
# # Save the graphlet kernel values to a text file
with open("graphlet_kernel.txt", "w") as f:
    f.write("Null")
#     for i, row in enumerate(graphlet_kernel):
#         for j, value in enumerate(row):
#             f.write(f"Kernel between node {i} and node {j}: {value}\n")
with open('weisfeiler_kernel.txt', 'w') as f:
    f.write("Null")
