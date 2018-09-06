package com.abn.GDALProcess;

import org.gdal.gdal.Band;
import org.gdal.gdal.Dataset;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconst;

import java.util.ArrayList;
import java.util.List;

public class OpenRaster{
    public double[][] data;
    public int xColumn, xRow;
    public boolean dummyCodeLC;
    public int[] totalclass;
    public OpenRaster(String inputRaster){
        Dataset ds = gdal.Open(inputRaster, gdalconst.GA_Update);
        if (ds == null){ System.exit(-1); }
        Band band = ds.GetRasterBand(1);


        //ds.GetProjection();
        this.xColumn = band.getXSize(); this.xRow = band.getYSize();
        double[] datatemp = new double[1];
        this.data = new double[this.xRow][this.xColumn];
        totalclass = new int[4];
        for(int x = 0; x < totalclass.length; x++){totalclass[x] = 0;}
        for (int y = 0; y < this.xRow; y++) {
            for (int x = 0; x < this.xColumn; x++) {
                band.ReadRaster(x, y, 1, 1, datatemp);
                this.data[y][x] = datatemp[0];
            }
        }
        band.delete();
        ds.delete();
    }
    public NH[] GetSmallNH(CheckSample checkSample, boolean dummyCodeLC){
        NH[] getNHTemp = new NH[checkSample.smallSamples_1N.size()];
        for (int i = 0; i < checkSample.smallSamples_1N.size(); i++) {
            DataPointYX temp = (DataPointYX) checkSample.smallSamples_1N.get(i);
            getNHTemp[i] = new NH(temp.y, temp.x, this.data, dummyCodeLC);
        }
        return getNHTemp;
    }
    public NH[] GetCompletedNH(CheckSample checkSample, boolean dummyCodeLC){
        NH[] getNHTemp = new NH[checkSample.dataCompleted_1N.size()];
        for (int i = 0; i < checkSample.dataCompleted_1N.size(); i++) {
            DataPointYX temp = (DataPointYX) checkSample.dataCompleted_1N.get(i);
            getNHTemp[i] = new NH(temp.y, temp.x, this.data, dummyCodeLC);
        }
        return getNHTemp;
    }
    public void PrintData(CheckSample Csample){
        List tempList = (ArrayList<DataPointYX>) Csample.dataFull;
        for (int y = 0; y < tempList.size() ; y++) {
            DataPointYX temp1 = (DataPointYX) tempList.get(y);
            System.out.println(y+"  y "+ temp1.y+ " x "+ temp1.x+" "+data[temp1.y][temp1.x]);
        }
    }
}