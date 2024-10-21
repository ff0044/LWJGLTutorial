import java.nio.FloatBuffer
import org.lwjgl.system.MemoryUtil
import org.lwjgl.opengl.GL30.*
import org.tinylog.Logger

class Mesh(positions: FloatArray, indices: IntArray) {
    private var vaoId = 0
    private var vboId = 0
    private var vertexCount = 0

    init {
        var verticesBuffer: FloatBuffer? = null
        try {
            Logger.debug { "Allocating memory for vertices buffer with size: ${positions.size}" }
            verticesBuffer = MemoryUtil.memAllocFloat(positions.size)
            vertexCount = positions.size / 3
            Logger.debug { "Vertex count set to: $vertexCount" }
            verticesBuffer.put(positions).flip()
            Logger.debug { "Vertices buffer populated and flipped" }

            vaoId = glGenVertexArrays()
            Logger.debug { "Generated VAO with ID: $vaoId" }
            glBindVertexArray(vaoId)
            Logger.debug { "Bound VAO with ID: $vaoId" }

            vboId = glGenBuffers()
            Logger.debug { "Generated VBO with ID: $vboId" }
            glBindBuffer(GL_ARRAY_BUFFER, vboId)
            Logger.debug { "Bound VBO with ID: $vboId" }
            glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW)
            Logger.debug { "Buffer data set for VBO with ID: $vboId" }
            glEnableVertexAttribArray(0)
            Logger.debug { "Enabled vertex attribute array at index 0" }
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0)
            Logger.debug { "Vertex attribute pointer set for VBO with ID: $vboId" }
            glBindBuffer(GL_ARRAY_BUFFER, 0)
            Logger.debug { "Unbound VBO with ID: $vboId" }

            glBindVertexArray(0)
            Logger.debug { "Unbound VAO with ID: $vaoId" }
        } finally {
            if (verticesBuffer != null) {
                MemoryUtil.memFree(verticesBuffer)
                Logger.debug { "Freed memory for vertices buffer" }
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
        glBindVertexArray(vaoId)
        glDisableVertexAttribArray(0)
        Logger.debug { "Disabled vertex attribute array at index 0 for VAO with ID: $vaoId" }

        // Delete the VBO
        glBindBuffer(GL_ARRAY_BUFFER, 0)
        glDeleteBuffers(vboId)
        Logger.debug { "Deleted VBO with ID: $vboId" }

        // Delete the VAO
        glBindVertexArray(0)
        glDeleteVertexArrays(vaoId)
        Logger.debug { "Deleted VAO with ID: $vaoId" }
    }
}