package com.ff0044.engine;

import com.ff0044.engine.graph.Render;
import com.ff0044.engine.scene.Scene;

public interface IAppLogic {

    void cleanup();
    void init(Window window, Scene scene, Render render);
    void input(Window window, Scene scene, long diffTimeMillis);
    void update(Window window, Scene scene, long diffTimeMillis);

}
