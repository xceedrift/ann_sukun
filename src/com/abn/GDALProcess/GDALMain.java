package com.abn.GDALProcess;

public class GDALMain {
    public NHBase[] input;
    public NHBase[] output;
    public NHBase[] input2027;
    public TransitionRaster from2003to2015;
    public OpenRaster original2015;
    public OpenRaster molusce2027;

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
    public GDALMain(){
        String[] assetRaster = {
                "assets/sukun_lc_2003.tif",
                "assets/sukun_lc_2015.tif",
                "assets/sukun_vs_jalan.tif",
                "assets/sukun_vs_kelerengan.tif",
                "assets/sukun_vs_lt2015.tif",
                "assets/sukun_vs_sungai.tif",
                "assets/simulation2027.tif"};
        OpenRaster sukunLC2003 = new OpenRaster(assetRaster[0]);
        OpenRaster sukunLC2015 = new OpenRaster(assetRaster[1]);
        OpenRaster sukunVSjalan = new OpenRaster(assetRaster[2]);
        OpenRaster sukunVSkelerengan = new OpenRaster(assetRaster[3]);
        OpenRaster sukunVSlt2015 = new OpenRaster(assetRaster[4]);
        OpenRaster sukunVSsungai = new OpenRaster(assetRaster[5]);
        OpenRaster sukunMOLUSCE2027 = new OpenRaster(assetRaster[6]);
        CheckSample sukunSample = new CheckSample(sukunLC2003);
        //sukunSample.PrintListData();
        NHData LC2003_SmallNH = new NHData(sukunLC2003.GetSmallNH(sukunSample, true));
        //NHData LC2015_SmallNH = new NHData(sukunLC2015.GetSmallNH(sukunSample, true));
        //VSsungai_CompletedNH.PrintDataNeighborhood();
        //sukunVSjalan.PrintData(sukunSample);

        //dari2003ke2015.PrintData();
        //sukunLC2003.PrintData(sukunSample);
        JoinVS gabunganVS = new JoinVS(sukunVSjalan, sukunVSkelerengan, sukunVSlt2015, sukunVSsungai, sukunSample);
        //sukunLC2003.PrintLCTotal();
        //VSjalan_getCompletedNeighborhood.PrintDataNeighborhood();
        //LC2015_getCompletedNeighborhood.PrintDataNeighborhood();
        //LC2015_getUnCompletedNeighborhood.PrintDataNeighborhood();


        //for(int i = 0; i < metal.jalan.length; i++){
        //    System.out.println(i+" data "+metal.jalan[i].y+" "+metal.jalan[i].x+"  "+metal.jalan[i].data+"  "
        //            +metal.kelerengan[i].data+"  "+metal.lt2015[i].data+"  "+metal.sungai[i].data);
        //}
        //metal.PrintData(metal.jalanX);
        NHData jalan = new NHData(gabunganVS.GetSmallNH(gabunganVS.jalanX));
        NHData kelerengan = new NHData(gabunganVS.GetSmallNH(gabunganVS.kelerenganX));
        NHData lt2015 = new NHData(gabunganVS.GetSmallNH(gabunganVS.lt2015X));
        NHData sungai = new NHData(gabunganVS.GetSmallNH(gabunganVS.sungaiX));

        InputForANN joinAllSmall = new InputForANN();
        NHBase[] malang2015Input = joinAllSmall.ANN_Input(LC2003_SmallNH, jalan, kelerengan, lt2015, sungai, sukunSample.smallSamples_1N);
        //jalan.PrintDataNeighborhood();

        /*System.out.println("joinAll "+ malang2015Input.length);
        System.out.println("joinAll "+ malang2015Input[0].nData.length);
        for(int i = 0; i < malang2015Input.length;i++){
            System.out.println("joinAll input "+i+" "+malang2015Input[i].y+" "+malang2015Input[i].x);
            //System.out.println();
        }*/
        TransitionRaster dari2003ke2015 = new TransitionRaster(sukunLC2003, sukunLC2015, sukunSample);

        NHBase[] malang2015Output = joinAllSmall.ANN_Output(dari2003ke2015,sukunSample.smallSamples_1N);
        //System.out.println("malang20uput "+malang2015Output.length);
        /*for(int i = 0; i < malang2015Input.length;i++){
            System.out.println("joinAll output "+i+" "+malang2015Input[i].y+" "+malang2015Input[i].x+"    ");
            for(int j = 0; j < malang2015Output[i].nData.length;j++){
                System.out.println(j+" "+malang2015Output[i].nData[j]+" from "+
                        sukunLC2003.data[malang2015Input[i].y][malang2015Input[i].x]+" to "+
                        sukunLC2015.data[malang2015Input[i].y][malang2015Input[i].x]+" harusnya "+
                        PembandingTransisi(sukunLC2003.data[malang2015Input[i].y][malang2015Input[i].x],
                                sukunLC2015.data[malang2015Input[i].y][malang2015Input[i].x]));
            }
        }*/
        input = malang2015Input;
        output = malang2015Output;


        NHData LC2015_Full = new NHData(sukunLC2015.GetCompletedNH(sukunSample, true));
        NHData jalanFull = new NHData(gabunganVS.GetCompletedNH(gabunganVS.jalanX));
        NHData kelerenganFull = new NHData(gabunganVS.GetCompletedNH(gabunganVS.kelerenganX));
        NHData lt2015Full = new NHData(gabunganVS.GetCompletedNH(gabunganVS.lt2015X));
        NHData sungaiFull = new NHData(gabunganVS.GetCompletedNH(gabunganVS.sungaiX));

        InputForANN joinAllFull = new InputForANN();
        NHBase[] malang2027Input = joinAllFull.ANN_Input(LC2015_Full, jalanFull, kelerenganFull, lt2015Full, sungaiFull, sukunSample.dataCompleted_1N);
        input2027 = malang2027Input;
        from2003to2015 = dari2003ke2015;
        original2015 = sukunLC2015;
        molusce2027 = sukunMOLUSCE2027;
        System.out.println("input2027 "+ input2027.length);
    }
}
