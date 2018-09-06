package com.abn.ANNProcess;

import java.util.LinkedList;
import java.util.List;

public class ANN_Manager {
    ANN_Set setANN;
    List listSourceDataInput;
    List listSourceDataOutput;
    int[] weightIndex;//, neuronSourceIndex;
    boolean debug = false;
    double[] last_inputFP;
    double[] last_dWeight_inputHidden;
    double[] last_dWeight_hiddenOutput;
    double[] last_sigmoidHidden; //data untuk hidden layer untuk node, sigmoid tanh dan dsigmoid tanh\
    double[] last_dsigmoidHidden;
    double[] last_sigmoidOutput; //data untuk output untuk node, sigmoid tanh dan dsigmoid tanh
    double[] last_dsigmoidOutput;
    double[] weight;
    double[] last_weight, best_weight;
    ANN_Manager(ANN_Set setting){
        setANN = setting;
        listSourceDataInput = new LinkedList();
        listSourceDataOutput = new LinkedList();
        int nw_InputHidden = (setANN.nBiasInput+setANN.nInput)*setANN.nHiddenLayer;
        int nw_HiddenOutput = (setANN.nHiddenLayer*setANN.nOutput);
        weight = SetWeightArray((nw_InputHidden )+(nw_HiddenOutput),true);
        int[] tempIndex = {nw_InputHidden,nw_HiddenOutput}; //data sementara
        weightIndex = tempIndex;
        last_dWeight_inputHidden = new double[weightIndex[0]];
        last_dWeight_hiddenOutput = new double[weightIndex[1]];
        for(int i = 0;i < last_dWeight_inputHidden.length;i++){
            last_dWeight_inputHidden[i] = 0; }
        for(int i = 0;i < last_dWeight_hiddenOutput.length;i++){
            last_dWeight_hiddenOutput[i] = 0; }

    }
    double[] SetWeightArray(int totalWeight, boolean random){
        double[] temp = new double[totalWeight];
        for(int i = 0;i < totalWeight;i++){
            if(random==true) { temp[i] = Math.floor(((Math.random() * 0.5) - 0.25)*100)/100;}
            else { temp[i] = 0.0; }
        } return temp;
    }
    double sigmoid(double temp){
        return Math.tanh(temp);
    }
    double dsigmoid(double temp){ return (1-(Math.pow(Math.tanh(temp),2))); }

    double[] getInputDataSource(List sourceData, int index){
        double[] data = (double[])sourceData.get(index);
        double[] inputFP = new double[setANN.nInput+setANN.nBiasInput];
        for(int i = 0; i < (setANN.nInput+setANN.nBiasInput); i++){
            if(i == ((setANN.nInput+setANN.nBiasInput)-1)){
                inputFP[i] = 1.0;   //untuk nilai bias
            } else { inputFP[i] = data[i]; }
            if(debug == true) {
                System.out.print("inputFP " + inputFP[i]);
                System.out.print(" ");
                if (((i + 1) % 9) == 0) {
                    System.out.println();
                }
            }
        }
        return inputFP;
    }

    double[] getOutputDataSource(List sourceAll, int index){
        double[] sourceOutput = (double[])sourceAll.get(index);
        double[] targetOutput = new double[setANN.nOutput];
        if(debug == true) { System.out.println("neuronSourceIndex[1]" + setANN.nOutput); }
        for(int i = 0; i < targetOutput.length; i++){
            targetOutput[i] = sourceOutput[i];
            if(debug==true) { System.out.print("targetOutput " + targetOutput[i]); System.out.print(" ");
                if (((i + 1) % 9) == 0) { System.out.println(); } }
        }
        return targetOutput;
    }

    void forwardPropagation(List sourceData, int index){
        double[] inputFP2 = getInputDataSource(sourceData,index);
        last_inputFP = inputFP2;

        if(debug == true) {
            System.out.println();
            System.out.println("weightIndex 0 " + weightIndex[0]);
            System.out.println("weightIndex 1 " + weightIndex[1]);
            System.out.println("weightLenght " + weight.length);
        }
        double [] temp_sigmoidHidden = new double[setANN.nHiddenLayer];
        double [] temp_dsigmoidHidden = new double[setANN.nHiddenLayer];
        for(int i = 0; i < setANN.nHiddenLayer; i++){
            double tempCalc = 0.0;
            for(int j = 0; j < setANN.nInput+setANN.nBiasInput; j++){
                int weightHiddenIndex = j+(i*(setANN.nInput+setANN.nBiasInput));
                if(debug == true) {
                    System.out.println("weightHiddenIndex " + j + " " + i + " "
                            + weightHiddenIndex + " inputFP2 " + inputFP2[j]);
                }
                tempCalc = tempCalc +(weight[weightHiddenIndex]*inputFP2[j]);
            }
            temp_sigmoidHidden[i]=sigmoid(tempCalc);
            temp_dsigmoidHidden[i]=dsigmoid(tempCalc);
        }
        last_sigmoidHidden = temp_sigmoidHidden;
        last_dsigmoidHidden = temp_dsigmoidHidden;

        if(debug == true) {
            System.out.println("nOutput " + setANN.nOutput);
            System.out.println("nHiddenLayer " + setANN.nHiddenLayer);
        }
        double [] temp_sigmoidOutput = new double[setANN.nOutput];
        double [] temp_dsigmoidOutput = new double[setANN.nOutput];
        for(int i = 0; i < setANN.nOutput; i++){
            double tempCalc = 0.0;
            for(int j = 0; j < setANN.nHiddenLayer; j++){
                int weightOutputIndex = (j+i*setANN.nHiddenLayer)+weightIndex[0];
                if(debug == true) {
                    System.out.println("weightOutputIndex " + weightOutputIndex);
                }
                tempCalc = tempCalc+(weight[weightOutputIndex]*temp_sigmoidHidden[j]);
            }
            temp_sigmoidOutput[i] =sigmoid(tempCalc);
            temp_dsigmoidOutput[i]=dsigmoid(tempCalc);
        }
        last_sigmoidOutput = temp_sigmoidOutput;
        last_dsigmoidOutput = temp_dsigmoidOutput;
    }
    void backwardPropagation(List sourceAll,int index){
        double[] targetOutput = getOutputDataSource(sourceAll, index);

        double[] outputSumErrorMargin = new double [targetOutput.length];
        double[] deltaOutputSum = new double[targetOutput.length];
        for(int i = 0; i < targetOutput.length; i++){
            outputSumErrorMargin[i] = targetOutput[i]-last_sigmoidOutput[i];
            deltaOutputSum[i] = outputSumErrorMargin[i]*last_dsigmoidOutput[i];
            if(debug == true) {
                System.out.println("deltaOutputSum " + i + " " + deltaOutputSum[i]);
            }
        }

        double[] deltaHiddenSum = new double[setANN.nHiddenLayer];
        for(int i = 0; i < setANN.nHiddenLayer; i++){ //deltaHiddenSum..expect 18 value
            for(int j = 0; j < setANN.nOutput; j++){ //outputSumErrorMargin..expect 9 value
                int weightOutputIndex = weightIndex[0]+((setANN.nHiddenLayer*j)+i);
                deltaHiddenSum[i] = deltaHiddenSum[i]+
                        (weight[weightOutputIndex]*outputSumErrorMargin[j]*last_dsigmoidHidden[i]);
            }
        }
        for(int i = 0; i < setANN.nHiddenLayer; i++){
            if(debug == true) { System.out.println("deltaHiddenSum " + i + " " + deltaHiddenSum[i]); }}

        double[] temp_allWeight = new double[weight.length];
        double[] dWeightBP_inputHidden = new double[weightIndex[0]];
        for(int i = 0; i < last_inputFP.length;i++){
            for(int j = 0; j < deltaHiddenSum.length;j++){
                int indexBP = (last_inputFP.length*j)+i;
                dWeightBP_inputHidden[indexBP] = deltaHiddenSum[j]*last_inputFP[i];
                temp_allWeight[indexBP] = (dWeightBP_inputHidden[indexBP]*setANN.learningRate)+
                        (last_dWeight_inputHidden[indexBP]*setANN.momentum)+weight[indexBP];
                if(debug == true) {
                    System.out.println("indexBP " + indexBP + " dWeightBP " + dWeightBP_inputHidden[indexBP]);
                }
            }
        }
        double[] dWeightBP_hiddenOutput = new double[weightIndex[1]];
        for(int i = 0; i < last_sigmoidHidden.length;i++){
            for(int j = 0; j < deltaOutputSum.length;j++){
                int indexBP = (last_sigmoidHidden.length*j)+i;
                dWeightBP_hiddenOutput[indexBP] = deltaOutputSum[j]*last_sigmoidHidden[i];
                temp_allWeight[indexBP+weightIndex[0]]=(dWeightBP_hiddenOutput[indexBP]*setANN.learningRate)+
                        (last_dWeight_hiddenOutput[indexBP]*setANN.momentum)+weight[indexBP+weightIndex[0]];
                if(debug == true) {
                    System.out.println("indexBP " + indexBP + " dWeightBP " + dWeightBP_hiddenOutput[indexBP]);
                }
            }
        }
        last_dWeight_inputHidden = dWeightBP_inputHidden;
        last_dWeight_hiddenOutput = dWeightBP_hiddenOutput;
        weight = temp_allWeight;
    }
}