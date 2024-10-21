package tk.ff0044.lwkglTutorial.game

import Mesh
import org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN
import org.lwjgl.glfw.GLFW.GLFW_KEY_UP
import tk.ff0044.lwkglTutorial.engine.IGameLogic
import tk.ff0044.lwkglTutorial.engine.Window


class DummyGame : IGameLogic {

    private var direction: Int = 0
    private var color: Float = 0.0f
    private val renderer: Renderer = Renderer()
    lateinit var mesh : Mesh

    // ------------------------------------------ //
    val positions = floatArrayOf(
        -0.5f,  0.5f, 0.0f,
        -0.5f, -0.5f, 0.0f,
        0.5f, -0.5f, 0.0f,
        0.5f,  0.5f, 0.0f,
    )
    val indices = intArrayOf(
        0, 1, 3,
        3, 1, 2
    )
    val colors = floatArrayOf(
        0.5f, 0.0f, 0.0f,
        0.0f, 0.5f, 0.0f,
        0.0f, 0.0f, 0.5f,
        0.0f, 0.5f, 0.5f,
    )
    // ------------------------------------------ //

    @Throws(Exception::class)
    override fun init() {
        renderer.init()

        mesh = Mesh(positions, indices, colors)
    }

    override fun input(window: Window?) {
        if (window != null) {
            if (window.isKeyPressed(GLFW_KEY_UP)) {
                direction = 1
            } else if (window.isKeyPressed(GLFW_KEY_DOWN)) {
                direction = -1
            } else {
                direction = 0
            }
        }
    }

    override fun update(interval: Float) {
        color += direction * 0.01f
        if (color > 1) {
            color = 1.0f
        } else if (color < 0) {
            color = 0.0f
        }
    }

    override fun render(window: Window?) {
        if (window != null) {
            window.setClearColor(color, color, color, 1.0f)
            renderer.render(window, mesh)
        }
    }

    override fun cleanup() {
        renderer.cleanup()
    }
}