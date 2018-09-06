package com.abn;

import com.abn.ANNProcess.ANN_Training;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.gdal.gdal.Band;
import org.gdal.gdal.Dataset;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconst;

public class myGDX implements ApplicationListener {
	Pixmap pixmap;
	Texture texture;
	SpriteBatch batch;

    DataRaster rDataXY_2003;
    DataRaster rDataXY_2009;
    DataRaster rDataXY_2015;
    DataRaster rDataXY_jalan;
    DataRaster rDataXY_kelerengan;
    DataRaster rDataXY_lt2009;
    DataRaster rDataXY_lt2015;
    DataRaster rDataXY_sungai;
    ANN_Training annTraining;
    Boolean screenshoot;

    DataRasterDouble rDataXY_2027;

    private Viewport viewport;
    private Camera camera;

	@Override
	public void create() {
	    screenshoot = true;
        annTraining = new ANN_Training();

        camera = new PerspectiveCamera();
        viewport = new ExtendViewport(960, 540, camera);

		gdal.AllRegister();
        rDataXY_2003 = rasterToXYData("assets/simulation2027.tif");
        rDataXY_2003.drawPixel();
        rDataXY_2003.setPosition(0f,0f);


        //rDataXY_2009 = rasterToXYData("assets/sukun_lc_2009.tif");
        //rDataXY_2009.drawPixel();
        //rDataXY_2009.setPosition(rDataXY_2009.columnSize/2,540f- rDataXY_2009.rowSize/2f);

        rDataXY_2015 = rasterToXYData("assets/sukun_lc_2015.tif");
        rDataXY_2015.drawPixel();
        rDataXY_2015.setPosition(rDataXY_2015.columnSize/2,540f- rDataXY_2015.rowSize/2f);

        rDataXY_2027 = rasterToXYDataDouble("assets/sukun_lc_2015.tif",annTraining.data2027_final4class_final);
        rDataXY_2027.drawPixel();
        rDataXY_2027.setPosition(0f,0f);

        rDataXY_jalan = rasterToXYData("assets/sukun_vs_jalan.tif");
        rDataXY_jalan.drawPixelContinuos();
        rDataXY_jalan.setPosition(rDataXY_jalan.columnSize/2*2,540f- rDataXY_jalan.rowSize/2f);

        rDataXY_kelerengan = rasterToXYData("assets/sukun_vs_kelerengan.tif");
        rDataXY_kelerengan.drawPixelContinuos();
        rDataXY_kelerengan.setPosition(rDataXY_kelerengan.columnSize/2*3,540f- rDataXY_kelerengan.rowSize/2f);

        //rDataXY_lt2009 = rasterToXYData("assets/sukun_vs_lt2009.tif");
        //rDataXY_lt2009.drawPixelContinuos();
        //rDataXY_lt2009.setPosition(rDataXY_lt2009.columnSize/2*5,540f- rDataXY_lt2009.rowSize/2f);

        rDataXY_lt2015 = rasterToXYData("assets/sukun_vs_lt2015.tif");
        rDataXY_lt2015.drawPixelContinuos();
        rDataXY_lt2015.setPosition(rDataXY_kelerengan.columnSize/2*4,540f- rDataXY_lt2015.rowSize/2f);

        rDataXY_sungai = rasterToXYData("assets/sukun_vs_sungai.tif");
        rDataXY_sungai.drawPixel();
        rDataXY_sungai.setPosition(rDataXY_kelerengan.columnSize/2*5,540f- rDataXY_sungai.rowSize/2f);

		batch = new SpriteBatch();
        //Gdx.app.log("MyTag", "rDataXY_jalan "+rDataXY_jalan.maxValue);
        //Gdx.app.log("MyTag", "rDataXY_jalan "+rDataXY_jalan.minValue);
	}

	@Override
	public void dispose() {

		batch.dispose();
		rDataXY_2003.clearData();//rDataXY_2009.clearData();
        rDataXY_2015.clearData();
        rDataXY_jalan.clearData();
        rDataXY_kelerengan.clearData();//rDataXY_lt2009.clearData();
        rDataXY_lt2015.clearData();
        rDataXY_sungai.clearData();
        rDataXY_2027.clearData();
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0.6f, 0.6f, 0.6f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(rDataXY_2003.texture, rDataXY_2003.posX, rDataXY_2003.posY,
                rDataXY_2003.columnSize*2, rDataXY_2003.rowSize*2);
        batch.draw(rDataXY_2027.texture, (rDataXY_2015.columnSize*2)+100, rDataXY_2003.posY,
                rDataXY_2015.columnSize*2, rDataXY_2015.rowSize*2);
        //batch.draw(rDataXY_2009.texture, rDataXY_2009.posX, rDataXY_2009.posY,
        //        rDataXY_2009.columnSize/2, rDataXY_2009.rowSize/2);
        batch.draw(rDataXY_2015.texture, 1280-((rDataXY_kelerengan.columnSize/2)*2), 220,
                rDataXY_2015.columnSize/2, rDataXY_2015.rowSize/2);
        batch.draw(rDataXY_jalan.texture, 1280-((rDataXY_kelerengan.columnSize/2)*2), 0,
                rDataXY_jalan.columnSize/2, rDataXY_jalan.rowSize/2);
        batch.draw(rDataXY_kelerengan.texture, 1280-rDataXY_kelerengan.columnSize/2, 440,
                rDataXY_kelerengan.columnSize/2, rDataXY_kelerengan.rowSize/2);
        //batch.draw(rDataXY_lt2009.texture, rDataXY_lt2009.posX, rDataXY_lt2009.posY,
        //        rDataXY_lt2009.columnSize/2, rDataXY_lt2009.rowSize/2);
        batch.draw(rDataXY_lt2015.texture, 1280-rDataXY_kelerengan.columnSize/2, 220,
                rDataXY_lt2015.columnSize/2, rDataXY_lt2015.rowSize/2);
        batch.draw(rDataXY_sungai.texture, 1280-rDataXY_kelerengan.columnSize/2, 0,
                rDataXY_sungai.columnSize/2, rDataXY_sungai.rowSize/2);
		batch.end();
	}

	@Override
	public void resize(int width, int height) {viewport.update(width, height);}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

	class DataRaster {
	    double[][] rasterData;
	    int columnSize;
	    int rowSize;
	    float posX;
	    float posY;
	    String filename;
	    double minValue;
	    double maxValue;

        Pixmap pixmap;
        Texture texture;

        public void setPosition(float xPos, float yPos){
            posX = xPos; posY = yPos;
        }
        public void drawPixel(){
            pixmap = new Pixmap(columnSize, rowSize, Pixmap.Format.RGBA8888); // Pixmap.Format.RGBA8888);
            pixmap.setColor(Color.WHITE);
            pixmap.fill();
            int tempPixel;
            for(int i = 0; i < rowSize; i++) {
                for (int j = 0; j < columnSize; j++) {
                    tempPixel = (int)rasterData[i][j];
                    if(tempPixel==4){
                        pixmap.setColor(Color.YELLOW); 	pixmap.drawPixel(j, i);
                    } else if(tempPixel==3){
                        pixmap.setColor(Color.RED); pixmap.drawPixel(j, i);
                    } else if(tempPixel==2){
                        pixmap.setColor(Color.GREEN); pixmap.drawPixel(j, i);
                    } else if(tempPixel==1){
                        pixmap.setColor(Color.BLUE); pixmap.drawPixel(j, i);
                    } else {
                        pixmap.setColor(Color.BLACK); pixmap.drawPixel(j, i);
                    }
                }
            }
            String fileTempData = "assets/simulation2027.tif";
            if(fileTempData.equals(filename)){
                //Gdx.app.log("MyTag", "my informative message");
                FileHandle fh2;
                int counter2 = 0;
                do{
                    fh2 = new FileHandle("simulasi2027_originalMOLUSCE"+counter2+".png");
                    counter2++;
                }while (fh2.exists());
                PixmapIO.writePNG(fh2, pixmap);
            }
            texture = new Texture(pixmap);
            texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
            pixmap.dispose();
        }

        public void drawPixelContinuos(){
            pixmap = new Pixmap(columnSize, rowSize, Pixmap.Format.RGBA8888); // Pixmap.Format.RGBA8888);
            pixmap.setColor(Color.WHITE);
            pixmap.fill();
            float tempPixel;
            float colourGray;
            float valueMax = (float)maxValue;
            for(int i = 0; i < rowSize; i++) {
                for (int j = 0; j < columnSize; j++) {
                    tempPixel = (float)rasterData[i][j];
                    colourGray = (float) ((float)(tempPixel/valueMax)*1f);
                    pixmap.setColor(colourGray,colourGray,colourGray,1f);
                    pixmap.drawPixel(j, i);
                }
            }

            texture = new Texture(pixmap);
            texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
            pixmap.dispose();
        }

	    public void clearData(){
	        texture.dispose();
        }
    }

	public DataRaster rasterToXYData(String rasterFile){
	    DataRaster temporary = new DataRaster();
	    temporary.filename = rasterFile;
        Dataset ds1 = gdal.Open( rasterFile, gdalconst.GA_Update );
        if (ds1 == null){ Gdx.app.log("MyTag", "my informative message"); return null;}
        Band band1 = ds1.GetRasterBand(1);
        temporary.columnSize = band1.getXSize();
        temporary.rowSize    = band1.getYSize();
        double[] tempData = new double[1];
        double minD;
        double maxD;
        minD = maxD = tempData[0]= 0;
        temporary.rasterData = new double[temporary.rowSize][temporary.columnSize];
        for(int i = 0; i < temporary.rowSize ; i++) {
            for (int j = 0; j < temporary.columnSize; j++) {
                band1.ReadRaster(j, i, 1, 1, tempData);
                if(tempData[0]>maxD){ maxD = tempData[0];}
                if(tempData[0]<minD){
                    if(tempData[0]>=0) {
                        minD = tempData[0];
                    }
                }
                temporary.rasterData[i][j]=tempData[0];
            }
        }
        temporary.minValue = minD;
        temporary.maxValue = maxD;
        band1.delete(); ds1.delete();
        return temporary;
	}



    class DataRasterDouble {
        double[][] rasterData;
        int columnSize;
        int rowSize;
        float posX;
        float posY;
        String filename;
        double minValue;
        double maxValue;

        Pixmap pixmap;
        Texture texture;

        public void setPosition(float xPos, float yPos){
            posX = xPos; posY = yPos;
        }
        public void drawPixel(){
            pixmap = new Pixmap(columnSize, rowSize, Pixmap.Format.RGBA8888); // Pixmap.Format.RGBA8888);
            pixmap.setColor(Color.WHITE);
            pixmap.fill();
            int tempPixel;
            for(int i = 0; i < rowSize; i++) {
                for (int j = 0; j < columnSize; j++) {
                    tempPixel = (int)rasterData[i][j];
                    if(tempPixel==4){
                        pixmap.setColor(Color.YELLOW); 	pixmap.drawPixel(j, i);
                    } else if(tempPixel==3){
                        pixmap.setColor(Color.RED); pixmap.drawPixel(j, i);
                    } else if(tempPixel==2){
                        pixmap.setColor(Color.GREEN); pixmap.drawPixel(j, i);
                    } else if(tempPixel==1){
                        pixmap.setColor(Color.BLUE); pixmap.drawPixel(j, i);
                    } else {
                        pixmap.setColor(Color.BLACK); pixmap.drawPixel(j, i);
                    }
                }
            }
            FileHandle fh;
            int counter = 0;
            do{
                fh = new FileHandle("simulasi2027_manualProgramming"+counter+".png");
                counter++;
            }while (fh.exists());
            PixmapIO.writePNG(fh, pixmap);
            texture = new Texture(pixmap);
            texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
            pixmap.dispose();
        }

        public void drawPixelContinuos(){
            pixmap = new Pixmap(columnSize, rowSize, Pixmap.Format.RGBA8888); // Pixmap.Format.RGBA8888);
            pixmap.setColor(Color.WHITE);
            pixmap.fill();
            float tempPixel;
            float colourGray;
            float valueMax = (float)maxValue;
            for(int i = 0; i < rowSize; i++) {
                for (int j = 0; j < columnSize; j++) {
                    tempPixel = (float)rasterData[i][j];
                    colourGray = (float) ((float)(tempPixel/valueMax)*1f);
                    pixmap.setColor(colourGray,colourGray,colourGray,1f);
                    pixmap.drawPixel(j, i);
                }
            }
            texture = new Texture(pixmap);
            texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
            pixmap.dispose();
        }

        public void clearData(){
            texture.dispose();
        }
    }

    public DataRasterDouble rasterToXYDataDouble(String rasterFile, double[][] data2027){
        DataRasterDouble temporary = new DataRasterDouble();
        temporary.filename = rasterFile;
        Dataset ds1 = gdal.Open( rasterFile, gdalconst.GA_Update );
        if (ds1 == null){ Gdx.app.log("MyTag", "my informative message"); return null;}
        Band band1 = ds1.GetRasterBand(1);
        temporary.columnSize = band1.getXSize();
        temporary.rowSize    = band1.getYSize();
        double[] tempData = new double[1];
        double minD;
        double maxD;
        minD = maxD = tempData[0]= 0;
        temporary.rasterData = new double[temporary.rowSize][temporary.columnSize];
        for(int i = 0; i < temporary.rowSize ; i++) {
            for (int j = 0; j < temporary.columnSize; j++) {
                /*tempData[0] = data2027[i][j];
                //band1.ReadRaster(j, i, 1, 1, tempData);
                if(tempData[0]>maxD){ maxD = tempData[0];}
                if(tempData[0]<minD){
                    if(tempData[0]>=0) {
                        minD = tempData[0];
                    }
                }*/
                temporary.rasterData[i][j]=data2027[i][j];
            }
        }
        temporary.minValue = minD;
        temporary.maxValue = maxD;
        band1.delete(); ds1.delete();
        return temporary;
    }
}