package tk.ff0044.lwkglTutorial.game

import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30
import org.lwjgl.opengl.GL30.*
import org.lwjgl.system.MemoryUtil
import org.tinylog.Logger
import tk.ff0044.lwkglTutorial.engine.GameItem
import tk.ff0044.lwkglTutorial.engine.Window
import tk.ff0044.lwkglTutorial.engine.common.Utils
import tk.ff0044.lwkglTutorial.engine.graph.ShaderProgram
import tk.ff0044.lwkglTutorial.engine.graph.Transformation
import java.nio.FloatBuffer


class Renderer {

    val FOV: Float = Math.toRadians(60.0).toFloat()
    val Z_NEAR: Float = 0.01f
    val Z_FAR: Float = 1000f

    private val transformation: Transformation = Transformation()
    private val shaderProgram: ShaderProgram = ShaderProgram()

    @Throws(Exception::class)
    fun init(window:Window) {
        Logger.debug{"Loading shader at /shaders/shader.vert"}
        shaderProgram.createVertexShader(Utils.loadResource("/shaders/shader.vert"))
        Logger.debug{"Loading shader at /shaders/shader.frag"}
        shaderProgram.createFragmentShader(Utils.loadResource("/shaders/shader.frag"))
        Logger.debug{"Linking shaders"}
        shaderProgram.link()

        Logger.debug{"Creating uniform at projection and world matrix"}
        shaderProgram.createUniform("projectionMatrix")
        shaderProgram.createUniform("worldMatrix")
        window.setClearColor(0.0f, 0.0f, 0.0f, 0.0f)
        Logger.debug{"Successfully set the clear colour"}
    }

    private fun clear() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT)
    }

    fun render(window: Window, gameItems: Array<GameItem>) {
        clear()

        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight())
            window.setResized(false)
        }

        shaderProgram.bind()


        // Update projection Matrix
        val projectionMatrix = transformation.getProjectionMatrix(
            FOV,
            window.getWidth().toFloat(), window.getHeight().toFloat(), Z_NEAR, Z_FAR
        )
        shaderProgram.setUniform("projectionMatrix", projectionMatrix)


        // Render each gameItem
        for (gameItem in gameItems) {
            // Set world matrix for this item
            val worldMatrix = transformation.getWorldMatrix(
                gameItem.getPosition(),
                gameItem.getRotation(),
                gameItem.getScale()
            )
            shaderProgram.setUniform("worldMatrix", worldMatrix)
            // Render the mes for this game item
            gameItem.getMesh().render()
        }

        shaderProgram.unbind()
    }

    fun cleanup() {
        shaderProgram.cleanup()
    }
}