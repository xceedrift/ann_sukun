package com.abn.ANNProcess;

import com.abn.GDALProcess.GDALMain;
import com.abn.GDALProcess.SingleDataPoint;
import com.abn.GDALProcess.DataPointYX;
import org.gdal.gdal.gdal;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ANN_Training {
    ANN_Manager annData1;
    ANN_Set setting;
    double trainTotal,valTotal, sampleTotal;
    double errorTrain,errorVal;
    double last_trainError, last_valError, minValError;
    int counterFalse = 0;int counterTrue = 0;
    public double[][] data2027_final4class_final;

    double computeMlpError(List sourceData, int index){
        annData1.forwardPropagation(sourceData,index);
        double[] data = annData1.last_sigmoidOutput;
        double[] data2 = (double[])annData1.listSourceDataOutput.get(index);
        double[] dataTemp = new double[annData1.last_sigmoidOutput.length];

        double dataTempSum = 0;
        for(int i = 0; i < dataTemp.length; i++){
            dataTemp[i] = Math.pow(data2[i]-data[i],2);
            dataTempSum = dataTempSum + dataTemp[i];
        }
        double dataTempDivide = dataTempSum/dataTemp.length;
        //System.out.println("dataTempDivide "+dataTempDivide );
        return dataTempDivide;
    }

    void computePerformance(){
        double trainError = 0;
        //System.out.println("trainTotal "+trainTotal );
        for(int i = 0; i < trainTotal; i++){
            trainError = trainError + computeMlpError(annData1.listSourceDataInput, i);
        }
        double errorTrainTemp = trainError/trainTotal;
        errorTrain = errorTrainTemp;

        //val_index
        double valError = 0;
        for(int i = 0; i < valTotal; i++){//jadi index val dimulai dengan trainTotal+i
            valError = valError + computeMlpError(annData1.listSourceDataInput, (int)trainTotal+i);
            //optional check kappa per iterationval
            int outIndexTemp = getIndexMax(annData1.last_sigmoidOutput);
            //System.out.println("indexTemp out "+ outIndexTemp);
            double[] trueOutput = annData1.getOutputDataSource(annData1.listSourceDataOutput,(int)trainTotal+i);
            int answerIndexTemp = getIndexMax(trueOutput);
            //System.out.println("indexTemp answer "+ answerIndexTemp);
            if(outIndexTemp==answerIndexTemp){
                counterTrue++;
            } else {
                counterFalse++;
            }
        }
        double errorValTemp = valError/valTotal;
        errorVal = errorValTemp;
    }

    void trainEpoch(){
        for (int i = 0; i < trainTotal; i++) {
            int j = (int) Math.round(Math.random() * (2450 - 1));
            //System.out.println("random value " + j);
            annData1.forwardPropagation(annData1.listSourceDataInput, j);
            annData1.backwardPropagation(annData1.listSourceDataOutput, j);
        }
    }

    void train(){
        int maxIteration = (int) setting.maxIteration;
        for(int k = 0; k < maxIteration; k++) {
            trainEpoch();
            computePerformance();
            last_trainError = errorTrain;
            last_valError = errorVal;
            if( k == 0 ){ //untuk pemberian nilai permulaan untuk awal pelatihan
                minValError = last_valError;
            } else {
                if(last_valError < minValError){
                    minValError = last_valError;
                    annData1.best_weight = annData1.weight;
                }
            }
            System.out.println(k+ " computePerformance errorTrain\t"+errorTrain+"\t\terrorVal\t"+errorVal);
        }
        annData1.weight = annData1.best_weight;
    }

    //fungsi untuk mencari nilai index terbesar dari output ANN
    int getIndexMax(double[] inputArray){
        double maximumValue = inputArray[0]; int indexMax = 0;
        for(int i=0; i < inputArray.length; i++){
            if(inputArray[i] > maximumValue){
                indexMax = i; maximumValue = inputArray[i];
            }
        }
        return indexMax; //return maximumValue;
    }

    double getConfidence(double[] inputArray){
        double firstMaximumValue = inputArray[0];
        double secondMaximumValue = firstMaximumValue;
        int indexMax = 0;
        for(int i=0; i < inputArray.length; i++){
            if(inputArray[i] > firstMaximumValue){
                indexMax = i;
                secondMaximumValue = firstMaximumValue;
                firstMaximumValue = inputArray[i];
            }
        }
        double minSig = -1;
        double rangeSig = 2;
        secondMaximumValue = (1*((secondMaximumValue-minSig)/rangeSig))*100;
        firstMaximumValue = (1*((firstMaximumValue-minSig)/rangeSig))*100;
        return firstMaximumValue-secondMaximumValue; //return maximumValue;
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


    public ANN_Training(){
        long start = System.nanoTime();

        gdal.AllRegister();
        GDALMain main2015 = new GDALMain();
        System.out.println(main2015.input.length);
        System.out.println(main2015.output.length);
        System.out.println(main2015.input[0].nData.length);
        System.out.println(main2015.output[0].nData.length);
        System.out.println(main2015.input[0].y+" "+main2015.input[0].x);
        System.out.println(main2015.output[0].y+" "+main2015.input[0].x);


        setting = new ANN_Set(main2015.input[0].nData.length,
                main2015.output[0].nData.length,
                0.01,0.05,2000);

        annData1 = new ANN_Manager(setting);
        annData1.debug = false;
        sampleTotal = main2015.input.length;
        trainTotal = Math.floor(main2015.input.length*setting.trainPercent);
        valTotal = sampleTotal-trainTotal;

        for(int i = 0; i < sampleTotal; i++){
            annData1.listSourceDataInput.add(main2015.input[i].nData);
            annData1.listSourceDataOutput.add(main2015.output[i].nData);
        }
        train();
        System.out.println("computePerformance minValError "+ minValError);

        int counterTrue = 0;
        int counterFalse = 0;
        for(int i = 0;i < annData1.listSourceDataInput.size();i++) {
            annData1.forwardPropagation(annData1.listSourceDataInput, i);
            int a = getIndexMax(annData1.last_sigmoidOutput);
            int b = getIndexMax(main2015.output[i].nData);
            if(a == b){
                counterTrue++;
            } else {
                counterFalse++;
            }
        }

        System.out.println("counterTrue "+ counterTrue);
        System.out.println("counterFalse "+ counterFalse);
        List input2027 = new LinkedList();
        for(int i = 0; i < main2015.input2027.length; i++){
            input2027.add(main2015.input2027[i].nData);
        }
        System.out.println("input2027 "+ input2027.size());


        //annData1.forwardPropagation(input2027, 0);
        //double dataSig = (double)getIndexMax(annData1.last_sigmoidOutput);
        //System.out.println("dataSig "+ dataSig);

        List data = new ArrayList<SingleDataPoint>();
        //List data2 = new ArrayList<SingleDataPoint>();
        int[] data2027_counter= new int[16]; //from 0-15 scale oi
        for(int j = 0; j < 16; j++){
            data2027_counter[j] = 0;
        }
        int[] data2015_counter= new int[16]; //from 0-15 scale coy
        for(int j = 0; j < 16; j++){
            data2015_counter[j] = 0;
        }

        int xRow_2027 = main2015.original2015.xRow;
        int xColumn_2027 = main2015.original2015.xColumn;

        //List dataFull = main2015.from2003to2015.cSample.dataFull;
        double[][] data2027_prediction = new double[xRow_2027][xColumn_2027]; //16 jenis
        double[][] data2027_confidence = new double[xRow_2027][xColumn_2027]; //16 jenis
        for (int y = 0; y < xRow_2027; y++) {
            for (int x = 0; x < xColumn_2027; x++) {
                data2027_prediction[y][x] = 0;
                double pointValue = main2015.original2015.data[y][x];
                if(pointValue == 1){
                    data2027_prediction[y][x] = 1;
                } else if(pointValue == 2){
                    data2027_prediction[y][x] = 6;
                } else if(pointValue == 3){
                    data2027_prediction[y][x] = 11;
                } else if(pointValue == 4){
                    data2027_prediction[y][x] = 16;
                }
                data2027_confidence[y][x] = 0;
            }
        }

        for(int i = 0; i < input2027.size(); i++){
            SingleDataPoint temp = new SingleDataPoint();
            temp.y = main2015.input2027[i].y;
            temp.x = main2015.input2027[i].x;
            annData1.forwardPropagation(input2027, i);
            double dataSig = (double)getIndexMax(annData1.last_sigmoidOutput);
            data2027_prediction[temp.y][temp.x] = dataSig+1;//menghindari angka 0 ditambah 1 seperti input prediction

            double dataConfidence = getConfidence(annData1.last_sigmoidOutput);
            data2027_confidence[temp.y][temp.x] = dataConfidence;
            System.out.println(i+" "+temp.y+" "+temp.x+"  tNatrix: "+dataSig+ "  confidence: "+dataConfidence);
            for(int j = 0; j < 16; j++){
                if(j == dataSig){
                    data2027_counter[j]++;
                }
            }
        }


        List tempFull = main2015.from2003to2015.cSample.dataFull;
        double[][] tempMap = main2015.from2003to2015.no;
        for(int i = 0;i < tempFull.size();i++){
            DataPointYX pointTemp = (DataPointYX)tempFull.get(i);
            System.out.println(i + " x "+ pointTemp.y+ " y "+pointTemp.x+ " "+tempMap[pointTemp.y][pointTemp.x]);
            for(int j = 0; j < 16; j++){
                if(j == tempMap[pointTemp.y][pointTemp.x]){
                    data2015_counter[j]++;
                }
            }
        }

        for(int k = 0; k < data2015_counter.length;k++){
            System.out.println("counter2015 "+ k + " "+data2015_counter[k]);
        }
        System.out.println();
        for(int k = 0; k < data2027_counter.length;k++){
            System.out.println("counter2027 "+ k + " "+data2027_counter[k]);
        }

        for(int i = 0;i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (i == j) {
                    continue;
                }
                double mer = PembandingTransisi(i+1,1+j);
                System.out.println("x "+ (i+1) + "  "+" y "+ (j+1)+ "  "+mer +"  "+data2015_counter[(int)mer]);
            }
        }

        int[] data2027_counterV2= new int[16]; //from 1-16 scale oi
        for(int j = 0; j < 16; j++){
            data2027_counterV2[j] = 0;
        }

        int xcounter2027_prediction = 0;
        for(int y = 0; y < xRow_2027; y++){
            for(int x= 0; x < xColumn_2027; x++){
                if(data2027_prediction[y][x] > 0){
                    xcounter2027_prediction++;
                    System.out.println("data "+ y+" "+ x+" "+data2027_prediction[y][x]);

                    for(int j = 1; j <= 16; j++){
                        if(j == (int)data2027_prediction[y][x]){
                            data2027_counterV2[j-1]++;
                        }
                    }

                }
            }
        }

        for(int k = 0; k < data2027_counterV2.length;k++){
            System.out.println("counter2027 "+ (k+1) + " "+data2027_counterV2[k]);
        }

        System.out.println("nilai xcounter prediction"+ xcounter2027_prediction);

        double[][] data2027_transitionFinal = new double[xRow_2027][xColumn_2027]; //16 jenis
        for(int y = 0; y < xRow_2027; y++) {
            for (int x = 0; x < xColumn_2027; x++) {
                data2027_transitionFinal[y][x] = main2015.original2015.data[y][x];
            }
        }

        for(int i = 0; i < input2027.size(); i++) {
            SingleDataPoint temp = new SingleDataPoint();
            temp.y = main2015.input2027[i].y;
            temp.x = main2015.input2027[i].x;
            if(data2027_confidence[temp.y][temp.x]>0) {
                data2027_transitionFinal[temp.y][temp.x] = data2027_prediction[temp.y][temp.x];
            }
        }

        double[][] data2027_final4class = new double[xRow_2027][xColumn_2027];

        int[] data2027_final4class_counter = new int[4]; //from 0-3 scale oi tp ditambah 1 diakhir
        for(int j = 0; j < 4; j++){
            data2027_final4class_counter[j] = 0;
        }

        for (int y = 0; y < xRow_2027; y++) {
            for (int x = 0; x < xColumn_2027; x++) {
                data2027_final4class[y][x] = 0;
                int nilaiLC = (int)data2027_transitionFinal[y][x];
                if((nilaiLC == 1)|| (nilaiLC == 5)||(nilaiLC == 9)||(nilaiLC == 13)){
                    data2027_final4class[y][x] = 1.0;
                    data2027_final4class_counter[0]++;
                } else if((nilaiLC == 2)|| (nilaiLC == 6)||(nilaiLC == 10)||(nilaiLC == 14)){
                    data2027_final4class[y][x] = 2.0;
                    data2027_final4class_counter[1]++;
                } else if((nilaiLC == 3)|| (nilaiLC == 7)||(nilaiLC == 11)||(nilaiLC == 15)){
                    data2027_final4class[y][x] = 3.0;
                    data2027_final4class_counter[2]++;
                } else if((nilaiLC == 4)|| (nilaiLC == 8)||(nilaiLC == 12)||(nilaiLC == 16)){
                    data2027_final4class[y][x] = 4.0;
                    data2027_final4class_counter[3]++;
                }
            }
        }

        System.out.println();
        System.out.println("hasil akhir");
        for(int k = 0; k < data2027_final4class_counter.length;k++){
            System.out.println("counter2027_final4class "+ (k+1) + " "+data2027_final4class_counter[k]);
        }


        for(int i = 0;i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                double initFinalClass = PembandingTransisi(i+1,1+j);
                double nCount = data2015_counter[(int)initFinalClass];
                double placesCount = data2027_counterV2[(int)initFinalClass];

                System.out.println("x "+ (i+1) + "  "+" y "+ (j+1)+ "  "+(initFinalClass+1)
                        +"\t\t2003-2015\t\t"+nCount+"\t\t2015-2027\t"+placesCount);
            }
        }
        System.out.println();

        /*
        double[][] data2027_places1 = new double[xRow_2027][xColumn_2027];


        for(int i = 0;i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if(i == j){
                    continue;
                }
                double initFinalClass = PembandingTransisi(i+1,1+j);
                double nCount = data2015_counter[(int)initFinalClass];

                if(nCount==0){
                    continue;
                }
                double placesCount = data2027_counter[(int)initFinalClass];

                if (placesCount < nCount){
                    nCount = placesCount;
                }
                if(nCount>0){
                    for(int i = 0; i < input2027.size();i++){
                        SingleDataPoint temp = new SingleDataPoint();
                        temp.y = main2015.input2027[i].y;
                        temp.x = main2015.input2027[i].x;
                        data2027_confidence[temp.y][temp.x]==
                    }
                    data2027_confidence.length

                }
                System.out.println("x "+ (i+1) + "  "+" y "+ (j+1)+ "  "+(initFinalClass+1)
                        +"\t\t2003-2015\t\t"+nCount+"\t\t2015-2027\t"+placesCount);
            }
        }*/
        /*int bloCoounter = 0;
        for(int i = 0; i < input2027.size(); i++){
            SingleDataPoint temp = new SingleDataPoint();
            temp.y = main2015.input2027[i].y;
            temp.x = main2015.input2027[i].x;
            double random = Math.random()/1000;
            data2027_confidence[temp.y][temp.x]=data2027_confidence[temp.y][temp.x]+random;
            System.out.println(i+" "+temp.y+" "+temp.x+" confidence: "+data2027_confidence[temp.y][temp.x]+ " "+random);
            if(data2027_confidence[temp.y][temp.x]<0){
                bloCoounter++;
            }
        }
        System.out.println("blooCounter "+bloCoounter);
*/
        double[][] original2027MOLUSCE = new double[xRow_2027][xColumn_2027];
        for (int y = 0; y < xRow_2027; y++) {
            for (int x = 0; x < xColumn_2027; x++) {
                original2027MOLUSCE[y][x] = main2015.molusce2027.data[y][x];
            }
        }



        System.out.println("asdd "+PembandingTransisi(4,4)); //4x4 = 15
        System.out.println("asdd "+PembandingTransisi(3,3)); //3x3 = 10
        int[] counterKappaAkurasi = new int[4*4];
        for(int i = 0; i < counterKappaAkurasi.length;i++){
            counterKappaAkurasi[i]=0;
        }
        List dataFull = main2015.from2003to2015.cSample.dataFull;
        for(int i = 0; i < dataFull.size(); i++) {
            //SingleDataPoint temp = new SingleDataPoint();
            DataPointYX temp = (DataPointYX)dataFull.get(i);
            //System.out.println(temp.y+" "+temp.x+" "+original2027MOLUSCE[temp.y][temp.x]+" -> "+
            //        data2027_final4class[temp.y][temp.x]);
            int noTemp = (int)PembandingTransisi(data2027_final4class[temp.y][temp.x],original2027MOLUSCE[temp.y][temp.x]);
            counterKappaAkurasi[noTemp]++;
        }

        System.out.println("dasar hitungan kappa");
        for(int i = 0; i < counterKappaAkurasi.length;i++){
            System.out.println(i+" "+counterKappaAkurasi[i]);
        }

        double totalDataBenar=0;
        for(int i = 0;i< 4;i++){
            for(int j = 0; j <4;j++){
                int tempIndex = 0;
                if(i==j){
                    tempIndex = (int)PembandingTransisi(i+1,j+1);
                    totalDataBenar = totalDataBenar+counterKappaAkurasi[tempIndex];
                }

            }
        }

        double Akurasi = (totalDataBenar/dataFull.size())*100;
        System.out.println("nilai akurasi "+Akurasi+" persen");
        double[] horizonTemp = new double[4];
        double[] vertikalTemp = new double[4];

        for(int i = 0 ; i < 4; i++){
            for(int j = 0; j <4;j++){
                horizonTemp[i]=horizonTemp[i]+counterKappaAkurasi[(i*4)+j];
            }
        }

        for(int i = 0 ; i < 4; i++){
            for(int j = 0; j <4;j++){
                vertikalTemp[i]=vertikalTemp[i]+counterKappaAkurasi[i+(j*4)];
            }
        }

        double tempTotalHorizonVertikal= 0;
        double jumlahdata = dataFull.size();
        for(int i = 0; i < 4; i++){
            tempTotalHorizonVertikal=tempTotalHorizonVertikal+(horizonTemp[i]*vertikalTemp[i]);
            //System.out.println("horizonTemp "+horizonTemp[i]);
        }
        double kappa = ((jumlahdata*totalDataBenar)-tempTotalHorizonVertikal)/((Math.pow(jumlahdata,2))-tempTotalHorizonVertikal);
        //System.out.println("jumlahdata*totalDataBenar "+jumlahdata*totalDataBenar);
        //System.out.println("Math.pow(jumlahdata,2) "+Math.pow(jumlahdata,2));
        //System.out.println("tempTotalHorizonVertikal "+tempTotalHorizonVertikal);
        System.out.println("nilai kappa "+kappa);

        data2027_final4class_final = data2027_final4class;

        long estimated = System.nanoTime()-start;
        System.out.println("lama perhitungan "+estimated);
        System.out.println("lama perhitungan milidetik "+(float)estimated/1000000);
        System.out.println("lama perhitungan detik "+(float)(estimated/1000000000.0));
        System.out.println("lama perhitungan menit "+(float)((estimated/1000000000.0)/60.0));
    }
}
