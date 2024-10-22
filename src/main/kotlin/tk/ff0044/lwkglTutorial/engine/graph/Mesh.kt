package tk.ff0044.lwkglTutorial.engine.graph

import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30.*
import org.lwjgl.system.MemoryUtil
import org.lwjgl.system.MemoryUtil.memAllocFloat
import org.tinylog.Logger
import java.nio.FloatBuffer
import java.nio.IntBuffer

class Mesh(positions: FloatArray, colors : FloatArray, indices: IntArray, ) {
    private var vaoId = 0
    private var posVboId = 0
    private var idxVboId = 0
    private var colorVboId = 0
    private var vertexCount = 0

    init {
        init(positions, colors, indices)
    }

    fun init(positions: FloatArray, colors : FloatArray, indices: IntArray) {
        var posBuffer: FloatBuffer? = null
        var colorBuffer : FloatBuffer? = null
        var indicesBuffer: IntBuffer? = null
        try {
            Logger.debug { "Initializing vertex count" }
            vertexCount = indices.size

            Logger.debug { "Generating and binding VAO" }
            vaoId = glGenVertexArrays()
            glBindVertexArray(vaoId)

            // Position VBO
            Logger.debug { "Generating and binding Position VBO" }
            posVboId = glGenBuffers();
            posBuffer = MemoryUtil.memAllocFloat(positions.size);
            posBuffer.put(positions).flip();
            glBindBuffer(GL_ARRAY_BUFFER, posVboId);
            glBufferData(GL_ARRAY_BUFFER, posBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(0);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

            // Color VBO
            Logger.debug { "Generating and binding Colour VBO" }
            colorVboId = glGenBuffers();
            colorBuffer = MemoryUtil.memAllocFloat(colors.size);
            colorBuffer.put(colors).flip();
            glBindBuffer(GL_ARRAY_BUFFER, colorVboId);
            glBufferData(GL_ARRAY_BUFFER, colorBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(1);
            glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);

            // Index VBO
            Logger.debug { "Generating and binding Index VBO" }
            idxVboId = glGenBuffers();
            indicesBuffer = MemoryUtil.memAllocInt(indices.size);
            indicesBuffer.put(indices).flip();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, idxVboId);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

            Logger.debug { "Unbinding buffers" }
            glBindBuffer(GL_ARRAY_BUFFER, 0)
            glBindVertexArray(0)
        } catch (e : Exception) {
          Logger.error("An error has occured while creating a mesh: ${e.message}", e.printStackTrace())
        } finally {
            Logger.debug { "Freeing memory buffers" }
            if (posBuffer != null) {
                MemoryUtil.memFree(posBuffer);
            }
            if (colorBuffer != null) {
                MemoryUtil.memFree(colorBuffer);
            }
            if (indicesBuffer != null) {
                MemoryUtil.memFree(indicesBuffer);
            }
        }
    }

    fun getVaoId(): Int {
        return vaoId
    }

    fun getVertexCount(): Int {
        return vertexCount
    }

    fun cleanUp() {
        Logger.debug { "Disabling vertex attribute array" }
        glDisableVertexAttribArray(0)

        Logger.debug { "Deleting position VBO" }
        glBindBuffer(GL_ARRAY_BUFFER, 0)
        glDeleteBuffers(posVboId)

        Logger.debug { "Deleting index VBO" }
        glDeleteBuffers(idxVboId)

        Logger.debug { "Deleting VAO" }
        glBindVertexArray(0)
        glDeleteVertexArrays(vaoId)
    }

    fun render() {
        // Draw the mesh
        glBindVertexArray(getVaoId())

        glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0)

        // Restore state
        glBindVertexArray(0)
    }
}