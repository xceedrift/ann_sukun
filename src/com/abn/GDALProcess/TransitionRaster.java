package com.abn.GDALProcess;

import java.util.ArrayList;
import java.util.List;

public class TransitionRaster {
    public double[][] no;
    public int[] totalTransisi;
    public CheckSample cSample;
    public TransitionRaster(OpenRaster fromData, OpenRaster toData, CheckSample Csample){
        this.cSample = Csample;
        List tempList = (ArrayList<DataPointYX>) Csample.dataFull;
        totalTransisi = new int[16];
        no = new double[fromData.xRow][fromData.xColumn];
        for(int x = 0; x < totalTransisi.length; x++){totalTransisi[x] = 0;}
        //no = new SingleDataPoint[tempList.size()];
        for (int j = 0; j < tempList.size() ; j++) {
            DataPointYX temp1 = (DataPointYX) tempList.get(j);
            no[temp1.y][temp1.x] = PembandingTransisi(fromData.data[temp1.y][temp1.x], toData.data[temp1.y][temp1.x] );

            /*System.out.println(j+"  y "+ temp1.y+ " x "+ temp1.x+"  "+fromData.data[temp1.y][temp1.x]+ "  "
            +toData.data[temp1.y][temp1.x]+ "  "+ no[temp1.y][temp1.x]);*/

            for(int k = 0;k < totalTransisi.length; k++){
                if((int)no[temp1.y][temp1.x] == k){
                    totalTransisi[k]++;
                }
            }
        }
        /*for(int x = 0; x < totalTransisi.length; x++){
            System.out.println("totalTransisi "+x+" "+totalTransisi[x]);
        }*/
    }
    double PembandingTransisi(double fromData, double toData) {
        for(int i = 0; i < 4; i++){
            for(int j = 1; j <= 4; j++){
                if((fromData==(i+1))&&(toData==j)){
                    return (((i*4)+j)-1);
                }
            }
        }
        return 0;
    }
}
