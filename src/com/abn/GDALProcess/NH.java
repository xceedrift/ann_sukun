package com.abn.GDALProcess;

public class NH {
    int y,x;
    boolean dummycodeLC;
    double[] n;
    int nilaiNeighborhood = 9; //n=1 jadi total pixel 3x3
    int d = 3; //nilai digit dummycode
    public NH(int yA, int xA, double[][] rasterData, boolean dummycodeLC){
        this.y = yA; this.x = xA;
        this.dummycodeLC = dummycodeLC;
        double[] nTemp = new double[nilaiNeighborhood];
        nTemp[0] = rasterData[y - 1][x - 1]; nTemp[1] = rasterData[y - 1][x];  nTemp[2] = rasterData[y - 1][x + 1];
        nTemp[3] = rasterData[y][x - 1];     nTemp[4] = rasterData[y][x];      nTemp[5] = rasterData[y][x + 1];
        nTemp[6] = rasterData[y + 1][x - 1]; nTemp[7] = rasterData[y + 1][x];  nTemp[8] = rasterData[y + 1][x + 1];
        if(this.dummycodeLC){
            n = new double[nilaiNeighborhood*d];
            for(int i = 0;i < (nilaiNeighborhood); i++){
                if(nTemp[i] == 1){ n[(i*d)+0]=0; n[(i*d)+1]=0; n[(i*d)+2]=1;
                } else  if(nTemp[i] == 2){ n[(i*d)+0]=0; n[(i*d)+1]=1; n[(i*d)+2]=0;
                } else  if(nTemp[i] == 3){ n[(i*d)+0]=1; n[(i*d)+1]=0; n[(i*d)+2]=0;
                } else  if(nTemp[i] == 4){ n[(i*d)+0]=0; n[(i*d)+1]=0; n[(i*d)+2]=0;
                }
            }
        } else {
            n = ((double[]) nTemp).clone();
            this.d = 1;
            this.nilaiNeighborhood = 3;
        }
    }
}