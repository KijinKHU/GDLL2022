from sklearn.neural_network import MLPClassifier
from sklearn.naive_bayes import MultinomialNB
from sklearn.linear_model import LogisticRegression
from sklearn.ensemble import RandomForestClassifier
from sklearn.ensemble import GradientBoostingClassifier
from sklearn import preprocessing
from sklearn.model_selection import train_test_split
from sklearn import tree

class TrainingClassifiers:
    def applyLogistic(self, X_train, y_train, X_test):
        clf_logreg = LogisticRegression()
        clf_logreg.fit(X_train, y_train)
        y_pred_class = clf_logreg.predict(X_test)
        print("LR completed")
        return y_pred_class, clf_logreg

    def applyRandomForest(self, X_train, y_train, X_test):
        # clf_randomForest = RandomForestClassifier(n_estimators=382, criterion='entropy', max_features=116, max_depth=33, min_samples_split=5, min_samples_leaf=1 )
        clf_randomForest = RandomForestClassifier()
        clf_randomForest.fit(X_train, y_train)
        y_pred_class = clf_randomForest.predict(X_test)
        print("RF completed")
        return y_pred_class, clf_randomForest


    def applyGradientBoosting(self, X_train, y_train, X_test):
        clf_gb = GradientBoostingClassifier()
        clf_gb.fit(X_train, y_train)
        y_pred_class = clf_gb.predict(X_test)
        print("GB completed")
        return y_pred_class, clf_gb

    def applyMLP(self, X_train, y_train, X_test):
        clf_MLP = MLPClassifier(solver='sgd')
        clf_MLP.fit(X_train, y_train)
        y_pred_class = clf_MLP.predict(X_test)
        print("MLP completed")
        return y_pred_class, clf_MLP


    def applyDecisionTree(self, X_train, y_train, X_test):
        clf_dt = tree.DecisionTreeClassifier()
        clf_dt.fit(X_train, y_train)
        y_pred_class = clf_dt.predict(X_test)
        print("DT completed")
        return y_pred_class, clf_dt

    def labelEnocder(self, labels):
        le = preprocessing.LabelEncoder()
        y = le.fit_transform(labels)
        return y

    def prepareTrainTestData(self, X, y, testSize):
        X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=testSize)
        return X_train, X_test, y_train, y_test

