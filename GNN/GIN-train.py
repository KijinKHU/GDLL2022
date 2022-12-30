import torch
import networkx as nx
import torch.utils.data

from gnn.Convolution import GinConv, GIN

from gnn.Utilities import load_dataP,separate_data


class GraphDataset(torch.utils.data.Dataset):
    """ Levanta los datasets de Powerful-GNNS. """

    def __init__(self, dataset, degree_as_tag=False):
        self.data, self.classes = load_dataP(dataset, degree_as_tag)

        self.features = self.data[0].node_features.shape[1]

    def __len__(self):
        return len(self.data)

    def __getitem__(self, idx):
        graph = self.data[idx]
        adjacency_matrix = nx.adjacency_matrix(graph.g).todense()

        item = {}

        item['adjacency_matrix'] = torch.tensor(adjacency_matrix, dtype=torch.float32)
        item['node_features'] = graph.node_features
        item['label'] = graph.label

        return item


DS = GraphDataset('PROTEINS')
DL = torch.utils.data.DataLoader(DS)

model = GIN(input_dim=DS.features, hidden_dim=3, output_dim=DS.classes, n_layers=3)

criterion = torch.nn.CrossEntropyLoss()
optimizer = torch.optim.Adam(model.parameters(), lr=0.01)

EPOCHS = 10

for epoch in range(EPOCHS):
    running_loss = 0.0

    for i, batch in enumerate(DL):
        A = batch['adjacency_matrix']
        X = batch['node_features']
        labels = batch['label']

        optimizer.zero_grad()

        outputs = model(A, X)
        loss = criterion(outputs, labels)
        loss.backward()
        optimizer.step()

        running_loss += loss.item()

    print(f'{epoch} - loss: {running_loss / (i + 1)}')