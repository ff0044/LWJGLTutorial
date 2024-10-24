package tk.ff0044.lwkglTutorial.game

import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL30.*
import org.tinylog.Logger
import tk.ff0044.lwkglTutorial.engine.GameItem
import tk.ff0044.lwkglTutorial.engine.Window
import tk.ff0044.lwkglTutorial.engine.graph.Camera
import tk.ff0044.lwkglTutorial.engine.graph.Utils
import tk.ff0044.lwkglTutorial.engine.graph.ShaderProgram
import tk.ff0044.lwkglTutorial.engine.graph.Transformation


class Renderer {

    val FOV: Float = Math.toRadians(60.0).toFloat()
    val Z_NEAR: Float = 0.01f
    val Z_FAR: Float = 1000f

    private var transformation: Transformation = Transformation()
    lateinit private var shaderProgram: ShaderProgram

    @Throws(Exception::class)
    fun init(window:Window) {
        shaderProgram = ShaderProgram()
        Logger.debug{"Loading shader at /shaders/shader.vert"}
        shaderProgram.createVertexShader(Utils.loadResource("/shaders/shader.vert"))
        Logger.debug{"Loading shader at /shaders/shader.frag"}
        shaderProgram.createFragmentShader(Utils.loadResource("/shaders/shader.frag"))
        Logger.debug{"Linking shaders"}
        shaderProgram.link()

        shaderProgram.createUniform("projectionMatrix");
        shaderProgram.createUniform("modelViewMatrix");
        shaderProgram.createUniform("texture_sampler");
    }

    fun clear() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT)
    }

    fun render(window: Window, camera : Camera, gameItems: Array<GameItem>) {
        clear()

        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight())
            window.setResized(false)
        }

        shaderProgram.bind()

        // Update projection matrix
        val projectionMatrix = transformation.getProjectionMatrix(FOV, window.getWidth().toFloat(), window.getHeight().toFloat(), Z_NEAR, Z_FAR)
        shaderProgram.setUniform("projectionMatrix", projectionMatrix)

        // Update view matrix
        val viewMatrix = transformation.getViewMatrix(camera)

        shaderProgram.setUniform("texture_sampler", 0)
        // Render each game item
        for (gameItem : GameItem in gameItems) {
            // Set model view matrix for this item
            val modelViewMatrix = transformation.getModelViewMatrix(gameItem, viewMatrix)
            shaderProgram.setUniform("modelViewMatrix", modelViewMatrix)
            // Render the mesh for the game item
            gameItem.mesh.render()
        }

        shaderProgram.unbind()
    }

    fun cleanup() {
        Logger.debug{"Shader is being cleaned up"}
        val exitCode = shaderProgram.cleanup()
        if (exitCode == 1) {
            Logger.debug { "shaderProgram.cleanup() returned success" }
        } else {
            Logger.error{"Error has occurred while exiting shader program ; Will still continue closing"}
        }
    }
}