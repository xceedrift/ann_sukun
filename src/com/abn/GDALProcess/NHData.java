package com.abn.GDALProcess;

public class NHData {
    public NH[] getN;
    public boolean usingDummyCode;
    public NHData(NH[] data){
        getN = data;
        usingDummyCode = getN[0].dummycodeLC;
    }
    public void PrintDataNeighborhood(){
        int counter = 0;
        for(int k = 0; k < getN.length; k++){
            for(int i = 0; i < getN[k].n.length; i++){
                System.out.print(getN[k].n[i]+ " ");
                if((i+1)%getN[k].d ==0){System.out.print("  ");}
                if((i+1)%getN[k].nilaiNeighborhood==0){System.out.println();
                }
            }
            System.out.println((counter++)+" coordinate y:" + getN[k].y  + "\tx:" + getN[k].x);
            System.out.println();
        }
        System.out.println("dummyCode "+ this.usingDummyCode);
        System.out.println("panjang data "+this.getN.length);
    }
}
