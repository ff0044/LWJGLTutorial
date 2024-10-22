package tk.ff0044.lwkglTutorial.engine.graph

import org.joml.Matrix4f
import org.lwjgl.opengl.GL20.*
import org.lwjgl.system.MemoryStack
import org.tinylog.Logger

class ShaderProgram {
    private var programId : Int = 0
    private var vertexShaderId = 0
    private var fragmentShaderId = 0
    private var uniforms: MutableMap<String, Int> = HashMap()

    init {
        programId = glCreateProgram()
        if (programId == 0) {
            Logger.error { "Could not create Shader" }
            throw Exception("Could not create Shader")
        }

        val error = glGetError()
        if (error != GL_NO_ERROR) {
            Logger.error { "OpenGL error: $error" }
            throw Exception("OpenGL error: $error")
        }
    }

    @Throws(Exception::class)
    fun createUniform(uniformName: String) {
        val uniformLocation = glGetUniformLocation(programId, uniformName)
        if (uniformLocation < 0) {
            throw Exception("Could not find uniform: $uniformName")
        }
        uniforms[uniformName] = uniformLocation
    }

    @Throws(Exception::class)
    fun setUniform(uniformName: String, value: Matrix4f) {
        // Dump the matrix into a float buffer
        MemoryStack.stackPush().use { stack ->
            glUniformMatrix4fv(uniforms[uniformName]!!, false, value[stack.mallocFloat(16)])
        }
    }

    @Throws(Exception::class)
    fun createVertexShader(shaderCode: String) {
        vertexShaderId = createShader(shaderCode, GL_VERTEX_SHADER)
    }

    @Throws(Exception::class)
    fun createFragmentShader(shaderCode: String) {
        fragmentShaderId = createShader(shaderCode, GL_FRAGMENT_SHADER)
    }

    @Throws(Exception::class)
    private fun createShader(shaderCode: String, shaderType: Int): Int {
        val shaderId = glCreateShader(shaderType)
        if (shaderId == 0) {
            throw Exception("Error creating shader. Type: $shaderType")
        }

        glShaderSource(shaderId, shaderCode)
        glCompileShader(shaderId)

        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
            val infoLog = glGetShaderInfoLog(shaderId, 1024)
            glDeleteShader(shaderId)
            throw Exception("Error compiling Shader code: $infoLog")
        }

        glAttachShader(programId, shaderId)
        return shaderId
    }

    @Throws(Exception::class)
    fun link() {
        glLinkProgram(programId)
        if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
            val infoLog = glGetProgramInfoLog(programId, 1024)
            throw Exception("Error linking Shader code: $infoLog")
        }

        if (vertexShaderId != 0) {
            glDetachShader(programId, vertexShaderId)
        }
        if (fragmentShaderId != 0) {
            glDetachShader(programId, fragmentShaderId)
        }

        glValidateProgram(programId)
        if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
            Logger.warn{"Warning validating Shader code: " + glGetProgramInfoLog(programId, 1024)}
        }
    }

    fun bind() {
        glUseProgram(programId)
    }

    fun unbind() {
        glUseProgram(0)
    }

    fun cleanup() : Int {
        unbind()
        Logger.debug{"Shader program has been unbinded"}
        if (programId != 0) {
            glDeleteProgram(programId)
            Logger.debug{"Shader program has been deleted"}
        }
        return 1
    }
}