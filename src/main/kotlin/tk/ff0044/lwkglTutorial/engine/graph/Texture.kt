package tk.ff0044.lwkglTutorial.engine.graph

import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL30.glGenerateMipmap
import org.lwjgl.stb.STBImage.*
import org.lwjgl.system.MemoryStack
import org.tinylog.Logger
import tk.ff0044.lwkglTutorial.engine.FileError
import java.nio.ByteBuffer

class Texture(val id: Int) {
    constructor(fileName: String) : this(loadTexture(fileName))

    fun bind() {
        glBindTexture(GL_TEXTURE_2D, id)
    }

    fun cleanup() {
        glDeleteTextures(id)
    }

    companion object {
        @Throws(Exception::class)
        private fun loadTexture(fileName: String): Int {
            var width: Int
            var height: Int
            var buf: ByteBuffer?

            MemoryStack.stackPush().use { stack ->
                val w = stack.mallocInt(1)
                val h = stack.mallocInt(1)
                val channels = stack.mallocInt(1)

                buf = stbi_load(fileName, w, h, channels, 4)
                if (buf == null) {
                    Logger.error{"Image file [" + fileName + "] not loaded: ${stbi_failure_reason()}"; FileError()}
                    throw FileError()
                }

                /* Get width and height of image */
                width = w.get()
                height = h.get()
            }
            // Create a new OpenGL texture
            val textureId = glGenTextures()
            // Bind the texture
            glBindTexture(GL_TEXTURE_2D, textureId)

            // Tell OpenGL how to unpack the RGBA bytes. Each component is 1 byte size
            glPixelStorei(GL_UNPACK_ALIGNMENT, 1)

            //glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            //glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

            // Upload the texture data
            glTexImage2D(
                GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0,
                GL_RGBA, GL_UNSIGNED_BYTE, buf
            )
            // Generate Mip Map
            glGenerateMipmap(GL_TEXTURE_2D)

            stbi_image_free(buf)

            return textureId
        }
    }
}