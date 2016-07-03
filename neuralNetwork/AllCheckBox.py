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

def writtenAll(testDirect,reference):
    
    def dataImage(file,y,dictionary):
        image = Image.open(file)
        y.append(dictionary[file])
        data = np.asarray(image)
        data = data.min(axis=2)
        image.close()
        return np.array([data/np.float32(256)])

    os.chdir(reference)
    fin = open('test_result','r')
    dictionary = fin.readlines()
    fin.close()
    
    os.chdir(testDirect)
    testsFiles = os.listdir()
    
    dictionary = json.loads(dictionary[0])
    yTest = []

    files = [file for file in testsFiles]
    xTest = np.array([dataImage(file,yTest,dictionary) for file in files])
    
    return xTest ,np.asarray(yTest),testsFiles

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

def predictAll(testDirect,directNetWorkParams,referenceDirect):
    os.chdir(directNetWorkParams)
    input_var = T.tensor4('inputs')
    target_var = T.ivector('targets')
    network = build_cnn(input_var)
    with np.load('model.npz') as f:
        param_values = [f['arr_%d' % i] for i in range(len(f.files))]
    lasagne.layers.set_all_param_values(network, param_values)
    
    test_prediction = lasagne.layers.get_output(network, deterministic=True)

    test_fn = theano.function([input_var], [test_prediction])

    xTest,yTest,files = writtenAll(testDirect,referenceDirect)
    
    result = test_fn(xTest)[0]
    predict = []
    
    for ind in result:
        predict.append(list(ind).index(ind.max()))
        
    exception_images = []
    
    for i in range(0,len(predict)):
        if predict[i] != yTest[i]:
            exception_images.append((files[i],yTest[i],predict[i]))
         
    answers = []
    for i in range(0,len(predict)):
        answers.append((files[i],predict[i],result[i].max()))
    return answers, exception_images

if __name__ == '__main__':
    predicts, exception = predictAll(sys.argv[1],sys.argv[2],sys.argv[3])
    os.chdir(sys.argv[2])
    finP,finE = open('predicts','w'),open('exception','w')
    finP.readlines(json.dumps(predicts),file = finP)
    finE.readlines(json.dumps(exceptions), file = finE)
    finP.close()
    finP.close()