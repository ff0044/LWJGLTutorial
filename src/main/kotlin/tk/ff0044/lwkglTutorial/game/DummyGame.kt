package tk.ff0044.lwkglTutorial.game

import org.joml.Vector3f
import org.lwjgl.glfw.GLFW.*
import org.tinylog.Logger
import tk.ff0044.lwkglTutorial.engine.GameItem
import tk.ff0044.lwkglTutorial.engine.IGameLogic
import tk.ff0044.lwkglTutorial.engine.MouseInput
import tk.ff0044.lwkglTutorial.engine.Window
import tk.ff0044.lwkglTutorial.engine.graph.Camera
import tk.ff0044.lwkglTutorial.engine.graph.Mesh
import tk.ff0044.lwkglTutorial.engine.graph.Texture

class DummyGame : IGameLogic {
    private val MOUSE_SENSITIVITY: Float = 0.2f
    private val CAMERA_POS_STEP: Float = 0.05f


    private var cameraInc : Vector3f = Vector3f()
    private var camera : Camera = Camera()
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
        val gameItem1 = GameItem(mesh)
        gameItem1.scale = 0.5f
        gameItem1.setPosition(0f, 0f, -2f)
        val gameItem2 = GameItem(mesh)
        gameItem2.scale = 0.5f
        gameItem2.setPosition(0.5f, 0.5f, -2f)
        val gameItem3 = GameItem(mesh)
        gameItem3.scale = 0.5f
        gameItem3.setPosition(0f, 0f, -2.5f)
        val gameItem4 = GameItem(mesh)
        gameItem4.scale = 0.5f
        gameItem4.setPosition(0.5f, 0f, -2.5f)
        gameItems = arrayOf(gameItem1, gameItem2, gameItem3, gameItem4)
    }

    override fun input(window: Window, mouseInput: MouseInput) {
        cameraInc.set(0f, 0f, 0f)
        if (window.isKeyPressed(GLFW_KEY_W)) {
            cameraInc.z = -1f
        } else if (window.isKeyPressed(GLFW_KEY_S)) {
            cameraInc.z = 1f
        }
        if (window.isKeyPressed(GLFW_KEY_A)) {
            cameraInc.x = -1f
        } else if (window.isKeyPressed(GLFW_KEY_D)) {
            cameraInc.x = 1f
        }
        if (window.isKeyPressed(GLFW_KEY_LEFT_SHIFT)) {
            cameraInc.y = -1f
        } else if (window.isKeyPressed(GLFW_KEY_SPACE)) {
            cameraInc.y = 1f
        }
    }

    override fun update(interval: Float, mouseInput: MouseInput) {
        camera.movePosition(cameraInc.x * CAMERA_POS_STEP, cameraInc.y * CAMERA_POS_STEP, cameraInc.z * CAMERA_POS_STEP)

        val rotVec = mouseInput.displVec
        camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0f)

//        if (mouseInput.isInWindow()) {
//            Logger.debug{"Mouse is in window"}
//        }
    }

    override fun render(window: Window) {
        renderer.render(window, camera, gameItems)
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