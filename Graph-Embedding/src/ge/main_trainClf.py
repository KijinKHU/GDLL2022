import warnings
warnings.filterwarnings("ignore")
import numpy as np
import scipy.sparse as sp
from utils import loadEmbeddingWithClassLabels, train_test_split, classify, accuracy
# Load Embedding
path = "../data/cora - copy1.content.embedding"


embedding, labels = loadEmbeddingWithClassLabels("../data/cora - copy1.content.embedding")
X_train, X_test, y_train, y_test = train_test_split(embedding, labels, test_split = 0.33)


y_pred, clf = classify(X_train, y_train, X_test, classifier = "rf")
print("what is X_train[7] paper", clf.predict(X_train[0]))
print("GT", y_train[0])
# y_pred, clf = classify(X_train, y_train, X_test, classifier = "rf")
# Prepare Train test data
# X_train, X_test, y_train, y_test = tr.prepareTrainTestData(embedding, labels, 0.33)

# Choose one of the following classifier for training train a classifier


print("Accuracy:", accuracy(y_test, y_pred))
