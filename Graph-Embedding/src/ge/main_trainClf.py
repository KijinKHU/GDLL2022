import warnings
warnings.filterwarnings("ignore")
import numpy as np
import scipy.sparse as sp
import ge
# Load Embedding
path = "../data/cora - copy1.content.embedding"


embedding, labels = ge.loadEmbeddingWithClassLabels("../data/cora - copy1.content.embedding")
X_train, X_test, y_train, y_test = ge.train_test_split(embedding, labels, test_split = 0.33)


y_pred, clf = ge.classify(X_train, y_train, X_test, classifier = "gb")
# Prepare Train test data
# X_train, X_test, y_train, y_test = tr.prepareTrainTestData(embedding, labels, 0.33)

# Choose one of the following classifier for training train a classifier


print("Accuracy:", ge.accuracy(y_test, y_pred))
