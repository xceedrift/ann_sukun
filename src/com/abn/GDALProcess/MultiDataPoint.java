package com.abn.GDALProcess;

public class MultiDataPoint {
    public int y,x;
    public double[] nData;
    public MultiDataPoint(int total){
        nData = new double[total];
    }
}
