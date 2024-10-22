package tk.ff0044.lwkglTutorial.game

import org.lwjgl.glfw.GLFW.*
import tk.ff0044.lwkglTutorial.engine.GameItem
import tk.ff0044.lwkglTutorial.engine.IGameLogic
import tk.ff0044.lwkglTutorial.engine.Window
import tk.ff0044.lwkglTutorial.engine.graph.Mesh


class DummyGame : IGameLogic {

    // ------------------------------------------ //
    private var positions: FloatArray = floatArrayOf(
        -0.5f, 0.5f, 0.5f,
        -0.5f, -0.5f, 0.5f,
        0.5f, -0.5f, 0.5f,
        0.5f, 0.5f, 0.5f,
    )
    private var colors: FloatArray = floatArrayOf(
        0.5f, 0.0f, 0.0f,
        0.0f, 0.5f, 0.0f,
        0.0f, 0.0f, 0.5f,
        0.0f, 0.5f, 0.5f,
    )
    private var indices: IntArray = intArrayOf(
        0, 1, 3, 3, 1, 2,
    )
    // ------------------------------------------ //

    private var displxInc = 0
    private var displyInc = 0
    private var displzInc = 0
    private var scaleInc = 0
    private val renderer: Renderer = Renderer()
    private lateinit var gameItems: Array<GameItem>
    private lateinit var mesh : Mesh



    @Throws(Exception::class)
    override fun init(window : Window) {
        renderer.init(window)
        val mesh = Mesh(positions, colors, indices)
        val gameItem = GameItem(mesh)
        gameItem.setPosition(0f, 0f, -2f)
        gameItems = arrayOf(gameItem)
    }

    override fun input(window: Window) {
        displyInc = 0
        displxInc = 0
        displzInc = 0
        scaleInc = 0
        if (window.isKeyPressed(GLFW_KEY_UP)) {
            displyInc = 1
        } else if (window.isKeyPressed(GLFW_KEY_DOWN)) {
            displyInc = -1
        } else if (window.isKeyPressed(GLFW_KEY_LEFT)) {
            displxInc = -1
        } else if (window.isKeyPressed(GLFW_KEY_RIGHT)) {
            displxInc = 1
        } else if (window.isKeyPressed(GLFW_KEY_A)) {
            displzInc = -1
        } else if (window.isKeyPressed(GLFW_KEY_Q)) {
            displzInc = 1
        } else if (window.isKeyPressed(GLFW_KEY_Z)) {
            scaleInc = -1
        } else if (window.isKeyPressed(GLFW_KEY_X)) {
            scaleInc = 1
        }
    }

    override fun update(interval: Float) {
        for (gameItem in gameItems) {
            // Update position
            val itemPos = gameItem.getPosition()
            val posx = itemPos.x + displxInc * 0.01f
            val posy = itemPos.y + displyInc * 0.01f
            val posz = itemPos.z + displzInc * 0.01f
            gameItem.setPosition(posx, posy, posz)


            // Update scale
            var scale = gameItem.getScale()
            scale += scaleInc * 0.05f
            if (scale < 0) {
                scale = 0f
            }
            gameItem.setScale(scale)


            // Update rotation angle
            var rotation = gameItem.getRotation().z + 1.5f
            if (rotation > 360) {
                rotation = 0f
            }
            gameItem.setRotation(0f, 0f, rotation)
        }
    }

    override fun render(window: Window) {
        renderer.render(window, gameItems)
    }

    override fun cleanup() {
        renderer.cleanup()
        for (gameItem : GameItem in gameItems) {
            gameItem.getMesh().cleanUp()
        }
    }
}