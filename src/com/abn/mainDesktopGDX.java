package com.abn;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class mainDesktopGDX {
    public static void main(String[] args) {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "Lowokwaru Simulation ANN";
        cfg.width = 1280;
        cfg.height = 640;
        new LwjglApplication(new myGDX(), cfg);
    }
}