package com.abn.GDALProcess;

import java.util.ArrayList;
import java.util.List;

public class JoinVS {
    double[][]jalanX;
    double[][]kelerenganX;
    double[][]lt2015X;
    double[][]sungaiX;
    int xColumn, xRow;
    CheckSample checkSample;

    double getMaxValue(double[] inputArray){
        double maximumValue = inputArray[0];
        for(int i=0; i < inputArray.length; i++){
            if(inputArray[i] > maximumValue){
                maximumValue = inputArray[i];
            }
        }
        return maximumValue; //return maximumValue;
    }

    double getMinValue(double[] inputArray){
        double minimumValue = inputArray[0];
        for(int i=0; i < inputArray.length; i++){
            if(inputArray[i] < minimumValue){
                minimumValue = inputArray[i];
            }
        }
        return minimumValue; //return maximumValue;
    }


    public JoinVS(OpenRaster jalanTemp, OpenRaster kelerenganTemp, OpenRaster lt2015Temp,
                  OpenRaster sungaiTemp, CheckSample Csample){
        checkSample = Csample;
        xColumn = jalanTemp.xColumn;
        xRow = jalanTemp.xRow;
        jalanX      = new double[xRow][xColumn];//jalanTemp.data.clone();
        kelerenganX = new double[xRow][xColumn];//kelerenganTemp.data.clone();
        lt2015X     = new double[xRow][xColumn];//lt2015Temp.data.clone();
        sungaiX     = new double[xRow][xColumn];//sungaiTemp.data.clone();

        int xcounter = 0;
        double jumlahTotal = 0;
        List tempList = (ArrayList<DataPointYX>) Csample.dataFull;
        System.out.println();
        double maxValue = 0;
        double minValue = 0;

        for (int j = 0; j < tempList.size() ; j++) {
            DataPointYX temp = (DataPointYX) tempList.get(j);
            double gabunganVS = jalanTemp.data[temp.y][temp.x]+kelerenganTemp.data[temp.y][temp.x]+
                    lt2015Temp.data[temp.y][temp.x]+sungaiTemp.data[temp.y][temp.x];
            jumlahTotal = jumlahTotal+gabunganVS;
            //jumlahTotaljalan = jumlahTotaljalan+ jalanTemp.data[temp.y][temp.x];
            //jumlahKuadratTotal = jumlahKuadratTotal + Math.pow(gabunganVS,2);
            if(jalanTemp.data[temp.y][temp.x]>maxValue){
                maxValue = jalanTemp.data[temp.y][temp.x];
            } else if(kelerenganTemp.data[temp.y][temp.x]>maxValue){
                maxValue = kelerenganTemp.data[temp.y][temp.x];
            } else if(lt2015Temp.data[temp.y][temp.x]>maxValue){
                maxValue = lt2015Temp.data[temp.y][temp.x];
            } else if(sungaiTemp.data[temp.y][temp.x]>maxValue){
                maxValue = sungaiTemp.data[temp.y][temp.x];
            }

            if(jalanTemp.data[temp.y][temp.x]<minValue){
                minValue = jalanTemp.data[temp.y][temp.x];
            } else if(kelerenganTemp.data[temp.y][temp.x]<minValue){
                minValue = kelerenganTemp.data[temp.y][temp.x];
            } else if(lt2015Temp.data[temp.y][temp.x]<minValue){
                minValue = lt2015Temp.data[temp.y][temp.x];
            } else if(sungaiTemp.data[temp.y][temp.x]<minValue){
                minValue = sungaiTemp.data[temp.y][temp.x];
            }

            xcounter++;
            //System.out.print(y+" joinVS "+temp.y+" "+temp.x);
            //System.out.print("\t\t"+jalanTemp.data[temp.y][temp.x]);
            //System.out.print("\t\t"+kelerenganTemp.data[temp.y][temp.x]);
            //System.out.print("\t"+lt2015Temp.data[temp.y][temp.x]);
            //System.out.print("\t"+sungaiTemp.data[temp.y][temp.x]);
            //System.out.println();
        }
        /*
        System.out.println("jumlahTotal "+jumlahTotal);
        System.out.println("jumlahKuadratTotal "+jumlahKuadratTotal);
        */
        double jumlahUnit = (double)(xcounter*4);
        double rata_rata = jumlahTotal/jumlahUnit;
        double jumlahTotal2 = 0;

        for (int y = 0; y < tempList.size() ; y++) {
            DataPointYX temp = (DataPointYX) tempList.get(y);
            double gabunganVS1 =
                    Math.pow((jalanTemp.data[temp.y][temp.x]-rata_rata),2)+
                    Math.pow((kelerenganTemp.data[temp.y][temp.x]-rata_rata),2)+
                    Math.pow((lt2015Temp.data[temp.y][temp.x]-rata_rata),2)+
                    Math.pow((sungaiTemp.data[temp.y][temp.x]-rata_rata),2);
            jumlahTotal2 = jumlahTotal2+gabunganVS1;
            //jumlahTotaljalan = jumlahTotaljalan+ jalanTemp.data[temp.y][temp.x];
            //jumlahKuadratTotal = jumlahKuadratTotal + Math.pow(gabunganVS,2);
            //System.out.print(y+" joinVS "+temp.y+" "+temp.x);
            //System.out.print("\t\t"+jalanTemp.data[temp.y][temp.x]);
            //System.out.print("\t\t"+kelerenganTemp.data[temp.y][temp.x]);
            //System.out.print("\t"+lt2015Temp.data[temp.y][temp.x]);
            //System.out.print("\t"+sungaiTemp.data[temp.y][temp.x]);
            //System.out.println();
        }

        double standarDeviasi = Math.sqrt(jumlahTotal2/(jumlahUnit-1));

        //double jumlahUnit = (double)(xcounter*4);
        //double jumlahUnit_minSatu = jumlahUnit-1;
        //double jumlahTotal_dikuadratkan = Math.pow(jumlahTotal,2);
        //double standarDeviasi = Math.pow(((jumlahUnit*jumlahKuadratTotal)-jumlahTotal_dikuadratkan)
        //        /(jumlahUnit*(jumlahUnit_minSatu)), 0.5);
        //double rata_rata= jumlahTotal/jumlahUnit;

        //jalan       = new SingleDataPoint[tempList.size()];
        //kelerengan  = new SingleDataPoint[tempList.size()];
        //lt2015      = new SingleDataPoint[tempList.size()];
        //sungai      = new SingleDataPoint[tempList.size()];
        //rata_rata = 0;
        //standarDeviasi = 1;
        for(int v = 0; v <  tempList.size(); v++) {
            DataPointYX temp = (DataPointYX) tempList.get(v);
            jalanX[temp.y][temp.x]= Math.tanh((jalanTemp.data[temp.y][temp.x]-rata_rata)/standarDeviasi);
            kelerenganX[temp.y][temp.x] = Math.tanh((kelerenganTemp.data[temp.y][temp.x]-rata_rata)/standarDeviasi);
            lt2015X[temp.y][temp.x] = Math.tanh((lt2015Temp.data[temp.y][temp.x]-rata_rata)/standarDeviasi);
            sungaiX[temp.y][temp.x] = Math.tanh((sungaiTemp.data[temp.y][temp.x]-rata_rata)/standarDeviasi);
        }
        /*
        for(int v = 0; v <  tempList.size(); v++) {
            DataPointYX temp = (DataPointYX) tempList.get(v);


        }
        {
            jalan[v] = new SingleDataPoint(); kelerengan[v] = new SingleDataPoint();
            lt2015[v] = new SingleDataPoint(); sungai[v] = new SingleDataPoint();
            jalan[v].y = kelerengan[v].y = lt2015[v].y = sungai[v].y = temp.y;
            jalan[v].x = kelerengan[v].x = lt2015[v].x = sungai[v].x = temp.x;
            jalan[v].data = (jalanTemp.data[temp.y][temp.x]-rata_rata)/standarDeviasi;
            kelerengan[v].data = (kelerenganTemp.data[temp.y][temp.x]-rata_rata)/standarDeviasi;
            lt2015[v].data = (lt2015Temp.data[temp.y][temp.x]-rata_rata)/standarDeviasi;
            sungai[v].data = (sungaiTemp.data[temp.y][temp.x]-rata_rata)/standarDeviasi;
        }*/
        /*System.out.println("jumlahUnit "+jumlahUnit+" jumlahTotal "+jumlahTotal);
        System.out.println("rata-rata "+rata_rata);
        System.out.println("standardeeviasi"+standarDeviasi);*/
    }

    public void PrintData(double[][] data){
        List tempList = (ArrayList<DataPointYX>) this.checkSample.dataFull;
        for (int y = 0; y < tempList.size() ; y++) {
            DataPointYX temp1 = (DataPointYX) tempList.get(y);
            System.out.println(y+"  y "+ temp1.y+ " x "+ temp1.x+" "+data[temp1.y][temp1.x]);
        }
    }

    public NH[] GetSmallNH(double[][] data){
        NH[] getNHTemp = new NH[checkSample.smallSamples_1N.size()];
        for (int i = 0; i < checkSample.smallSamples_1N.size(); i++) {
            DataPointYX temp = (DataPointYX) checkSample.smallSamples_1N.get(i);
            getNHTemp[i] = new NH(temp.y, temp.x, data, false);
        }
        return getNHTemp;
    }

    public NH[] GetCompletedNH(double[][] data){
        NH[] getNHTemp = new NH[checkSample.dataCompleted_1N.size()];
        for (int i = 0; i < checkSample.dataCompleted_1N.size(); i++) {
            DataPointYX temp = (DataPointYX) checkSample.dataCompleted_1N.get(i);
            getNHTemp[i] = new NH(temp.y, temp.x, data, false);
        }
        return getNHTemp;
    }
}
