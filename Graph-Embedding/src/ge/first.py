import pickle
pkl_file = open('../data/deepwalk_encoder.pkl', 'rb')
le_departure = pickle.load(pkl_file)
pkl_file.close()
print( le_departure.transform([]))