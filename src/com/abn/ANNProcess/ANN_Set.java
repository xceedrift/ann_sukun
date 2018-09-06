package com.abn.ANNProcess;

public class ANN_Set {
    int nInput; int nOutput; double learningRate; double momentum; double maxIteration;
    int nHiddenLayer;int nBiasInput; double trainPercent; double valPercent;
    ANN_Set(int nInput, int nOutput, double lRate, double momentum, double mIteration){
        nHiddenLayer = (int) 32;//Math.round(Math.sqrt(nInput*nOutput));
        this.nInput = nInput;
        this.nOutput = nOutput;
        this.learningRate=lRate;
        this.momentum = momentum;
        this.maxIteration = mIteration;
        this.nBiasInput = 1;
        this.trainPercent = 0.8;
        this.valPercent = 1.0 - trainPercent;
    }
}

