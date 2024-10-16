package com.ff0044;

import com.ff0044.engine.Engine;
import com.ff0044.engine.IAppLogic;
import com.ff0044.engine.Window;
import com.ff0044.engine.graph.Mesh;
import com.ff0044.engine.graph.Render;
import com.ff0044.engine.scene.Scene;

public class Main implements IAppLogic {
    public static void main(String[] args) {
        Main main = new Main();
        Engine gameEngine = new Engine("Hello LWJGL", new Window.WindowOptions(), main);
        gameEngine.start();
    }


    @Override
    public void cleanup() {

    }

    @Override
    public void init(Window window, Scene scene, Render render) {
        float[] positions = new float[] {
                0.0f, 0.5f, 0.0f,
                -0.5f, -0.5f, 0.0f,
                0.5f, -0.5f, 0.0f
        };
        Mesh mesh = new Mesh(positions, 3);
        scene.addMesh("triangle", mesh);
    }

    @Override
    public void input(Window window, Scene scene, long diffTimeMillis) {

    }

    @Override
    public void update(Window window, Scene scene, long diffTimeMillis) {

    }
}