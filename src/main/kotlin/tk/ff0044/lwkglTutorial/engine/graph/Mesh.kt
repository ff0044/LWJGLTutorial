package tk.ff0044.lwkglTutorial.engine.graph

import org.lwjgl.system.MemoryUtil

import java.nio.FloatBuffer
import java.nio.IntBuffer
import java.util.ArrayList

import org.lwjgl.opengl.GL11.GL_FLOAT
import org.lwjgl.opengl.GL11.GL_TEXTURE_2D
import org.lwjgl.opengl.GL11.GL_TRIANGLES
import org.lwjgl.opengl.GL11.GL_UNSIGNED_INT
import org.lwjgl.opengl.GL11.glBindTexture
import org.lwjgl.opengl.GL11.glDrawElements
import org.lwjgl.opengl.GL13.GL_TEXTURE0
import org.lwjgl.opengl.GL13.glActiveTexture
import org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER
import org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER
import org.lwjgl.opengl.GL15.GL_STATIC_DRAW
import org.lwjgl.opengl.GL15.glBindBuffer
import org.lwjgl.opengl.GL15.glBufferData
import org.lwjgl.opengl.GL15.glDeleteBuffers
import org.lwjgl.opengl.GL15.glGenBuffers
import org.lwjgl.opengl.GL20.glDisableVertexAttribArray
import org.lwjgl.opengl.GL20.glEnableVertexAttribArray
import org.lwjgl.opengl.GL20.glVertexAttribPointer
import org.lwjgl.opengl.GL30.glBindVertexArray
import org.lwjgl.opengl.GL30.glDeleteVertexArrays
import org.lwjgl.opengl.GL30.glGenVertexArrays

import org.tinylog.Logger


class Mesh(positions: FloatArray, var textCoords: FloatArray, indices: IntArray, var texture : Texture) {
    private var vaoId = 0

    private var vboIdList: MutableList<Int> = mutableListOf()

    private var vertexCount = 0

    init {
        var posBuffer: FloatBuffer? = null
        var textCoordsBuffer: FloatBuffer? = null
        var indicesBuffer: IntBuffer? = null
        try {
            Logger.debug { "Setting vertex count" }
            vertexCount = indices.size

            Logger.debug { "Generating and binding VAO" }
            vaoId = glGenVertexArrays()
            glBindVertexArray(vaoId)

            // Position VBO
            Logger.debug { "Generating Position VBO" }
            var vboId = glGenBuffers()
            vboIdList.add(vboId)
            posBuffer = MemoryUtil.memAllocFloat(positions.size)
            posBuffer.put(positions).flip()
            glBindBuffer(GL_ARRAY_BUFFER, vboId)
            glBufferData(GL_ARRAY_BUFFER, posBuffer, GL_STATIC_DRAW)
            glEnableVertexAttribArray(0)
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0)

            // Texture coordinates VBO
            Logger.debug { "Generating Texture coordinates VBO" }
            vboId = glGenBuffers()
            vboIdList.add(vboId)
            textCoordsBuffer = MemoryUtil.memAllocFloat(textCoords.size)
            textCoordsBuffer.put(textCoords).flip()
            glBindBuffer(GL_ARRAY_BUFFER, vboId)
            glBufferData(GL_ARRAY_BUFFER, textCoordsBuffer, GL_STATIC_DRAW)
            glEnableVertexAttribArray(1)
            glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0)

            // Index VBO
            Logger.debug { "Generating Index VBO" }
            vboId = glGenBuffers()
            vboIdList.add(vboId)
            indicesBuffer = MemoryUtil.memAllocInt(indices.size)
            indicesBuffer.put(indices).flip()
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboId)
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW)

            Logger.debug { "Unbinding buffers" }
            glBindBuffer(GL_ARRAY_BUFFER, 0)
            glBindVertexArray(0)
        } finally {
            Logger.debug { "Freeing buffers" }
            if (posBuffer != null) {
                MemoryUtil.memFree(posBuffer)
            }
            if (textCoordsBuffer != null) {
                MemoryUtil.memFree(textCoordsBuffer)
            }
            if (indicesBuffer != null) {
                MemoryUtil.memFree(indicesBuffer)
            }
        }
    }


    fun render() {
        // Activate first texture unit
        glActiveTexture(GL_TEXTURE0);
        // Bind the texture
        glBindTexture(GL_TEXTURE_2D, texture.id);

        glBindVertexArray(vaoId)

        glDrawElements(GL_TRIANGLES, vertexCount, GL_UNSIGNED_INT, 0)

        glBindVertexArray(0)

    }

    fun cleanUp() {
        Logger.debug{"Disabling vertex attribute array"}
        glDisableVertexAttribArray(0)
        Logger.debug{"Successfully disabled"}

        // Delete the VBOs
        Logger.debug{"Deleting VBO List"}
        glBindBuffer(GL_ARRAY_BUFFER, 0)
        for (vboId in vboIdList) {
            glDeleteBuffers(vboId)
            Logger.debug{"Deleted $vboId"}
        }
        Logger.debug{"Successfully deleted VBO List"}


        // Delete the texture
        texture.cleanup()
        Logger.debug{"Cleaning up texture by accessing function"}

        // Delete the VAO
        Logger.debug{"Deleting VAO"}
        glBindVertexArray(0)
        glDeleteVertexArrays(vaoId)
    }
}