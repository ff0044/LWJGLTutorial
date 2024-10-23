package tk.ff0044.lwkglTutorial.game

import org.lwjgl.glfw.GLFW.*
import org.tinylog.Logger
import tk.ff0044.lwkglTutorial.engine.GameItem
import tk.ff0044.lwkglTutorial.engine.IGameLogic
import tk.ff0044.lwkglTutorial.engine.Window
import tk.ff0044.lwkglTutorial.engine.graph.Mesh
import tk.ff0044.lwkglTutorial.engine.graph.Texture


class DummyGame : IGameLogic {
    private var displxInc = 0
    private var displyInc = 0
    private var displzInc = 0
    private var scaleInc = 0
    private val renderer = Renderer()
    private lateinit var gameItems: Array<GameItem>

    @Throws(Exception::class)
    override fun init(window: Window) {
        Logger.debug { "Initializing DummyGame" }
        renderer.init(window!!)
        Logger.debug { "Renderer initialized" }

        // Create the Mesh
        Logger.debug { "Creating Mesh" }
        val positions = floatArrayOf(
            // V0
            -0.5f, 0.5f, 0.5f,
            // V1
            -0.5f, -0.5f, 0.5f,
            // V2
            0.5f, -0.5f, 0.5f,
            // V3
            0.5f, 0.5f, 0.5f,
            // V4
            -0.5f, 0.5f, -0.5f,
            // V5
            0.5f, 0.5f, -0.5f,
            // V6
            -0.5f, -0.5f, -0.5f,
            // V7
            0.5f, -0.5f, -0.5f,

            // For text coords in top face
            // V8: V4 repeated
            -0.5f, 0.5f, -0.5f,
            // V9: V5 repeated
            0.5f, 0.5f, -0.5f,
            // V10: V0 repeated
            -0.5f, 0.5f, 0.5f,
            // V11: V3 repeated
            0.5f, 0.5f, 0.5f,

            // For text coords in right face
            // V12: V3 repeated
            0.5f, 0.5f, 0.5f,
            // V13: V2 repeated
            0.5f, -0.5f, 0.5f,

            // For text coords in left face
            // V14: V0 repeated
            -0.5f, 0.5f, 0.5f,
            // V15: V1 repeated
            -0.5f, -0.5f, 0.5f,

            // For text coords in bottom face
            // V16: V6 repeated
            -0.5f, -0.5f, -0.5f,
            // V17: V7 repeated
            0.5f, -0.5f, -0.5f,
            // V18: V1 repeated
            -0.5f, -0.5f, 0.5f,
            // V19: V2 repeated
            0.5f, -0.5f, 0.5f,
        )
        val textCoords = floatArrayOf(
            0.0f, 0.0f,
            0.0f, 0.5f,
            0.5f, 0.5f,
            0.5f, 0.0f,

            0.0f, 0.0f,
            0.5f, 0.0f,
            0.0f, 0.5f,
            0.5f, 0.5f,

            // For text coords in top face
            0.0f, 0.5f,
            0.5f, 0.5f,
            0.0f, 1.0f,
            0.5f, 1.0f,

            // For text coords in right face
            0.0f, 0.0f,
            0.0f, 0.5f,

            // For text coords in left face
            0.5f, 0.0f,
            0.5f, 0.5f,

            // For text coords in bottom face
            0.5f, 0.0f,
            1.0f, 0.0f,
            0.5f, 0.5f,
            1.0f, 0.5f,
        )
        val indices = intArrayOf(
            // Front face
            0, 1, 3, 3, 1, 2,
            // Top Face
            8, 10, 11, 9, 8, 11,
            // Right face
            12, 13, 7, 5, 12, 7,
            // Left face
            14, 15, 6, 4, 14, 6,
            // Bottom face
            16, 18, 19, 17, 16, 19,
            // Back face
            4, 6, 7, 5, 4, 7,
        )

        val texture = Texture("textures/grassblock.png")
        val mesh = Mesh(positions, textCoords, indices, texture)
        val gameItem = GameItem(mesh)
        gameItem.setPosition(0f, 0f, -2f)
        gameItems = arrayOf(gameItem)
        Logger.debug { "Mesh created and game item initialized" }
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
            val itemPos = gameItem.position
            val posx = itemPos.x + displxInc * 0.01f
            val posy = itemPos.y + displyInc * 0.01f
            val posz = itemPos.z + displzInc * 0.01f
            gameItem.setPosition(posx, posy, posz)


            // Update scale
            var scale = gameItem.scale
            scale += scaleInc * 0.05f
            if (scale < 0) {
                scale = 0f
            }
            gameItem.scale = scale

            // Update rotation angle
            var rotation = gameItem.rotation.x + 1.5f
            if (rotation > 360) {
                rotation = 0f
            }
            gameItem.setRotation(rotation, rotation, rotation)
        }
    }

    override fun render(window: Window) {
        renderer.render(window!!, gameItems)
    }

    override fun cleanup() {
        Logger.debug { "Cleaning up resources" }
        renderer.cleanup()
        for (gameItem in gameItems) {
            gameItem.mesh.cleanUp()
        }
        Logger.debug { "Cleanup complete" }
    }
}