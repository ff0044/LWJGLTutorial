package tk.ff0044.lwkglTutorial.engine.graph

import org.lwjgl.opengl.GL30.*
import org.lwjgl.system.MemoryUtil
import org.lwjgl.system.MemoryUtil.memAllocFloat
import org.tinylog.Logger
import java.nio.FloatBuffer
import java.nio.IntBuffer

class Mesh(positions: FloatArray, colours: FloatArray, indices: IntArray) {
    var vaoId: Int = 0

    private var posVboId = 0

    private var colourVboId = 0

    private var idxVboId = 0

    var vertexCount: Int = 0

    init {
        var posBuffer: FloatBuffer? = null
        var colourBuffer: FloatBuffer? = null
        var indicesBuffer: IntBuffer? = null
        try {
            vertexCount = indices.size
            Logger.debug { "Vertex count set to \${vertexCount}" }

            vaoId = glGenVertexArrays()
            Logger.debug { "Generated VAO with ID \${vaoId}" }
            glBindVertexArray(vaoId)
            Logger.debug { "Bound VAO with ID \${vaoId}" }

            // Position VBO
            posVboId = glGenBuffers()
            Logger.debug { "Generated Position VBO with ID \${posVboId}" }
            posBuffer = memAllocFloat(positions.size)
            posBuffer.put(positions).flip()
            glBindBuffer(GL_ARRAY_BUFFER, posVboId)
            Logger.debug { "Bound Position VBO with ID \${posVboId}" }
            glBufferData(GL_ARRAY_BUFFER, posBuffer, GL_STATIC_DRAW)
            Logger.debug { "Position data uploaded to VBO" }
            glEnableVertexAttribArray(0)
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0)
            Logger.debug { "Position VBO configured" }

            // Colour VBO
            colourVboId = glGenBuffers()
            Logger.debug { "Generated Colour VBO with ID \${colourVboId}" }
            colourBuffer = memAllocFloat(colours.size)
            colourBuffer.put(colours).flip()
            glBindBuffer(GL_ARRAY_BUFFER, colourVboId)
            Logger.debug { "Bound Colour VBO with ID \${colourVboId}" }
            glBufferData(GL_ARRAY_BUFFER, colourBuffer, GL_STATIC_DRAW)
            Logger.debug { "Colour data uploaded to VBO" }
            glEnableVertexAttribArray(1)
            glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0)
            Logger.debug { "Colour VBO configured" }

            // Index VBO
            idxVboId = glGenBuffers()
            Logger.debug { "Generated Index VBO with ID \${idxVboId}" }
            indicesBuffer = MemoryUtil.memAllocInt(indices.size)
            indicesBuffer.put(indices).flip()
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, idxVboId)
            Logger.debug { "Bound Index VBO with ID \${idxVboId}" }
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW)
            Logger.debug { "Index data uploaded to VBO" }

            glBindBuffer(GL_ARRAY_BUFFER, 0)
            glBindVertexArray(0)
            Logger.debug { "Unbound VBO and VAO" }
        } finally {
            if (posBuffer != null) {
                MemoryUtil.memFree(posBuffer)
                Logger.debug { "Freed Position Buffer" }
            }
            if (colourBuffer != null) {
                MemoryUtil.memFree(colourBuffer)
                Logger.debug { "Freed Colour Buffer" }
            }
            if (indicesBuffer != null) {
                MemoryUtil.memFree(indicesBuffer)
                Logger.debug { "Freed Index Buffer" }
            }
        }
    }

    fun render() {
        // Draw the mesh
        glBindVertexArray(vaoId)

        // Draw the elements of the vertex
        glDrawElements(GL_TRIANGLES, vertexCount, GL_UNSIGNED_INT, 0)

        // Restore state
        glBindVertexArray(0)
    }

    fun cleanUp() {
        Logger.debug { "Disabling vertex attribute array" }
        glDisableVertexAttribArray(0)

        // Delete the VBOs
        Logger.debug { "Deleting VBOs" }
        glBindBuffer(GL_ARRAY_BUFFER, 0)
        glDeleteBuffers(posVboId)
        glDeleteBuffers(colourVboId)
        glDeleteBuffers(idxVboId)

        // Delete the VAO
        Logger.debug { "Deleting VAO" }
        glBindVertexArray(0)
        glDeleteVertexArrays(vaoId)
    }
}