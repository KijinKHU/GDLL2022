import warnings
warnings.filterwarnings("ignore")

from utils import loadEmbedding, train_test_split, classify, accuracy

# Load Embedding
path = "../data/cora - copy1.content.embedding"
embedding, labels = loadEmbedding("../data/cora - copy1.content.embedding")

# Prepare Train test data
X_train, X_test, y_train, y_test = train_test_split(embedding, labels, test_split = 0.33)


# learn classification
y_pred, clf = classify(X_train, y_train, X_test, classifier = "lr")


# Choose one of the following classifier for training train a classifier
print("Accuracy:", accuracy(y_test, y_pred))
