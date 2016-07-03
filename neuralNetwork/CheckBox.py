from __future__ import print_function

import sys
import os
import time

import numpy as np
import theano
import theano.tensor as T

import lasagne
import json
from PIL import Image
from random import shuffle

def written(file):
    image = Image.open(file)
    data = np.asarray(image)
    data = data.min(axis=2)
    image.close()
    data = np.array([data/np.float32(256)])
    xTest = np.array([data])
    return xTest

def build_cnn(input_var=None):
    network = lasagne.layers.InputLayer(shape=(None, 1, 38, 38),
                                        input_var=input_var)
    network = lasagne.layers.Conv2DLayer(
            network, num_filters=32, filter_size=(5, 5),
            nonlinearity=lasagne.nonlinearities.rectify,
            W=lasagne.init.GlorotUniform())
    #nonlinearity indicate function activitaion
    network = lasagne.layers.MaxPool2DLayer(network, pool_size=(2, 2))
    
    network = lasagne.layers.Conv2DLayer(
            network, num_filters=32, filter_size=(5, 5),
            nonlinearity=lasagne.nonlinearities.rectify)
    network = lasagne.layers.MaxPool2DLayer(network, pool_size=(2, 2))
    
    network = lasagne.layers.DenseLayer(
            lasagne.layers.dropout(network, p=.3),
            num_units = 256,
            nonlinearity=lasagne.nonlinearities.rectify)
    
    network = lasagne.layers.DenseLayer(
            lasagne.layers.dropout(network, p=.3),
            num_units=3,
            nonlinearity=lasagne.nonlinearities.softmax)
    
    return network


def predict(file,directNetWorkParams):
    os.chdir(directNetWorkParams)
    input_var = T.tensor4('inputs')
    target_var = T.ivector('targets')
    network = build_cnn(input_var)
    with np.load('model.npz') as f:
        param_values = [f['arr_%d' % i] for i in range(len(f.files))]
    lasagne.layers.set_all_param_values(network, param_values)
    
    test_prediction = lasagne.layers.get_output(network, deterministic=True)

    test_fe = theano.function([input_var], [test_prediction])

    xTest = written(file)
    
    result = test_fe(xTest)
    result = result[0][0]
    val = result.max()
    return list(result).index(val)

if __name__ == '__main__':
    print(predict(sys.argv[1],sys.argv[2]))