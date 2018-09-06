package com.abn.GDALProcess;

import java.util.List;

public class InputForANN {
    public InputForANN(){}
    public NHBase[] ANN_Input(NHData LandCover, NHData jalan, NHData kelerengan,
                              NHData lahanTerbangun, NHData sungai, List kindOfSamples){
        List listTemp = kindOfSamples;
        NHBase[] dataTemp = new NHBase[listTemp.size()];
        for(int v = 0; v < dataTemp.length; v++) {
            DataPointYX temp2 = (DataPointYX)listTemp.get(v);
            dataTemp[v] = new NHBase((4*9)+(3*9));
            dataTemp[v].y = temp2.y;//lowokwaruLC2003.getN[v].y;//normalisasiVS[v].y;
            dataTemp[v].x = temp2.x;//lowokwaruLC2003.getN[v].x;//normalisasiVS[v].x;
            for (int u = 0; u < dataTemp[v].nData.length; u++) {
                if ((u >= 0) && (u <= 26)) {
                    dataTemp[v].nData[u] = LandCover.getN[v].n[u];
                } else if ((u >= 27) && (u <= 35)) {
                    dataTemp[v].nData[u] = jalan.getN[v].n[u-27];
                } else if ((u >= 36) && (u <= 44)) {
                    dataTemp[v].nData[u] = kelerengan.getN[v].n[u-36];
                } else if ((u >= 45) && (u <= 53)) {
                    dataTemp[v].nData[u] = lahanTerbangun.getN[v].n[u-45];
                } else if ((u >= 54) && (u <= 62)) {
                    dataTemp[v].nData[u] = sungai.getN[v].n[u-54];
                }
            }
        }
        return dataTemp;
    }

    public NHBase[] ANN_Output(TransitionRaster xTemp, List kindOfSamples){
        double[][]noTrans = xTemp.no;
        List listSample = kindOfSamples; //small sample
        NHBase[] smallTransitionTemp = new NHBase[listSample.size()];
        for(int i = 0;i < listSample.size(); i++){
            DataPointYX pointSample = (DataPointYX)listSample.get(i);
            smallTransitionTemp[i] = new NHBase(16);
            smallTransitionTemp[i].y = pointSample.y;
            smallTransitionTemp[i].x = pointSample.x;
            for (int u = 0; u < smallTransitionTemp[i].nData.length; u++) {
                smallTransitionTemp[i].nData[u] = -1;
            }
            for(int index = 0; index < smallTransitionTemp[i].nData.length;index++){
                if(noTrans[pointSample.y][pointSample.x] == index){
                    smallTransitionTemp[i].nData[index] = 1;
                }
            }
        }
        return smallTransitionTemp;
    }
}
