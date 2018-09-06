package com.abn.GDALProcess;

import java.util.ArrayList;
import java.util.List;

public class CheckSample{
    public int sampleTotal = 2450;
    public double percentageSamples = 0.10;
    public List dataFull;
    public List dataNull;
    public List dataCompleted_1N;
    public List smallSamples_1N;
    public List dataUnCompleted_1N; //small sample menggunakan 2569 pixel dari 25685
    public CheckSample(OpenRaster openRaster){
        this.dataFull = new ArrayList<DataPointYX>();
        this.dataCompleted_1N = new ArrayList<DataPointYX>();
        this.dataUnCompleted_1N = new ArrayList<DataPointYX>();
        this.dataNull = new ArrayList<DataPointYX>();
        for (int y = 0; y < openRaster.xRow; y++) {
            for (int x = 0; x < openRaster.xColumn; x++) {
                if (openRaster.data[y][x] > 0) {
                    this.dataFull.add(new DataPointYX(y, x));
                    if ((openRaster.data[y - 1][x - 1] == 0) || (openRaster.data[y - 1][x] == 0) || (openRaster.data[y - 1][x + 1] == 0) ||
                            (openRaster.data[y][x - 1] == 0) || (openRaster.data[y][x] == 0) || (openRaster.data[y][x + 1] == 0) ||
                            (openRaster.data[y + 1][x - 1] == 0) || (openRaster.data[y + 1][x] == 0) || (openRaster.data[y + 1][x + 1] == 0)) {
                        this.dataUnCompleted_1N.add(new DataPointYX(y, x));
                    } else {
                        this.dataCompleted_1N.add(new DataPointYX(y, x));
                    }
                } else {
                    this.dataNull.add(new DataPointYX(y, x));
                }
            }
        }
        this.sampleTotal = (int)(Math.round(this.dataFull.size()*percentageSamples));
        List randomSamples = (ArrayList<DataPointYX>) ((ArrayList) this.dataCompleted_1N).clone();
        smallSamples_1N = new ArrayList<DataPointYX>();
        for (int k = 0; k < this.sampleTotal; k++) {
            int tempRandom = randomValue(0, randomSamples.size() - 1);
            smallSamples_1N.add(randomSamples.get(tempRandom));
            randomSamples.remove(tempRandom);
        }
        randomSamples.clear();
    }
    int randomValue(int min, int max){ return (int) (Math.random() * ((max - min) + 1)) + min; }
    void PrintSample1N_YX(){
        //int count = 0;
        for(int k = 0; k < this.smallSamples_1N.size(); k++){
            DataPointYX temp = (DataPointYX) this.smallSamples_1N.get(k);
            System.out.println(" coordinate y:" + temp.y + "\tx:" + temp.x + "\t");
            //System.out.println(count++);
        }
    }
    public void PrintListData(){
        System.out.println("lowokwaruSample.smallSamples_1N.size "+this.smallSamples_1N.size());
        System.out.println("lowokwaruSample.dataCompleted_1N.size "+this.dataCompleted_1N.size());
        System.out.println("lowokwaruSample.dataUnCompleted_1N.size "+this.dataUnCompleted_1N.size());
        System.out.println("lowokwaruSample.dataFull.size "+this.dataFull.size());
        System.out.println("lowokwaruSample.dataNull.size "+this.dataNull.size());
    }
}
