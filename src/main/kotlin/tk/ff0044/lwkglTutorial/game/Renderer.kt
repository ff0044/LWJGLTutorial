package tk.ff0044.lwkglTutorial.game

import Mesh
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30
import org.lwjgl.opengl.GL30.glBindVertexArray
import org.lwjgl.system.MemoryUtil
import tk.ff0044.lwkglTutorial.engine.Window
import tk.ff0044.lwkglTutorial.engine.common.Utils
import tk.ff0044.lwkglTutorial.engine.graph.ShaderProgram
import java.nio.FloatBuffer


class Renderer {
    private var vboId = 0

    private var vaoId = 0

    private lateinit var shaderProgram: ShaderProgram

    @Throws(Exception::class)
    fun init() {
        shaderProgram = ShaderProgram()
        shaderProgram.createVertexShader(Utils.loadResource("/shaders/shader.vert"))
        shaderProgram.createFragmentShader(Utils.loadResource("/shaders/shader.frag"))
        shaderProgram.link()

        val vertices = floatArrayOf(
            0.0f, 0.5f, 0.0f,
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f
        )

        var verticesBuffer: FloatBuffer? = null
        try {
            verticesBuffer = MemoryUtil.memAllocFloat(vertices.size)
            verticesBuffer.put(vertices).flip()

            // Create the VAO and bind to it
            vaoId = GL30.glGenVertexArrays()
            GL30.glBindVertexArray(vaoId)

            // Create the VBO and bind to it
            vboId = GL15.glGenBuffers()
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId)
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_STATIC_DRAW)
            // Enable location 0
            GL20.glEnableVertexAttribArray(0)
            // Define structure of the data
            GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0)

            // Unbind the VBO
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0)

            // Unbind the VAO
            GL30.glBindVertexArray(0)
        } finally {
            if (verticesBuffer != null) {
                MemoryUtil.memFree(verticesBuffer)
            }
        }
    }

    private fun clear() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT)
    }

    fun render(window : Window, mesh: Mesh) {
        clear()

        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight())
            window.setResized(false)
        }

        shaderProgram.bind()

        // Draw the mesh
        glBindVertexArray(mesh.getVaoId())
        glDrawArrays(GL_TRIANGLES, 0, mesh.getVertexCount())

        // Restore state
        glBindVertexArray(0)

        shaderProgram.unbind()
    }

    fun cleanup() {
        shaderProgram.cleanup()
    }
}