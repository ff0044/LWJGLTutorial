package tk.ff0044.lwkglTutorial.engine.graph

import org.lwjgl.opengl.GL20
import org.tinylog.Logger


class ShaderProgram {
    private val programId = GL20.glCreateProgram()

    private var vertexShaderId = 0

    private var fragmentShaderId = 0

    init {
        if (programId == 0) {
            Logger.error("Could not create Shader", Exception().printStackTrace())
            throw Exception()
        }
    }

    @Throws(Exception::class)
    fun createVertexShader(shaderCode: String?) {
        Logger.debug{"Creating vertex shader"}
        vertexShaderId = createShader(shaderCode, GL20.GL_VERTEX_SHADER)
    }

    @Throws(Exception::class)
    fun createFragmentShader(shaderCode: String?) {
        Logger.debug{"Creating fragment shader"}
        fragmentShaderId = createShader(shaderCode, GL20.GL_FRAGMENT_SHADER)
    }

    @Throws(Exception::class)
    protected fun createShader(shaderCode: String?, shaderType: Int): Int {
        Logger.debug{"Creating shader"}
        val shaderId = GL20.glCreateShader(shaderType)
        if (shaderId == 0) {
            Logger.error("Error creating shader. Type: $shaderType", Exception())
            throw Exception()
        }

        Logger.debug{"Referencing shader source"}
        GL20.glShaderSource(shaderId, shaderCode)
        Logger.info{"Compiling Shaders"}
        GL20.glCompileShader(shaderId)

        Logger.info{"Checking compile status"}
        var compileStatus = GL20.glGetShaderi(shaderId, GL20.GL_COMPILE_STATUS)
        Logger.info{"Compile status: $compileStatus"}
        if ( compileStatus == 0) {
            Logger.error("Error compiling Shader code: " + GL20.glGetShaderInfoLog(shaderId, 1024), Exception())
            throw Exception()
        }

        Logger.debug{"Attaching shader to program"}
        GL20.glAttachShader(programId, shaderId)

        return shaderId
    }

    @Throws(Exception::class)
    fun link() {
        Logger.debug{""}
        GL20.glLinkProgram(programId)
        if (GL20.glGetProgrami(programId, GL20.GL_LINK_STATUS) == 0) {
            Logger.error("Error linking Shader code: " + GL20.glGetProgramInfoLog(programId, 1024), Exception())
            throw Exception()
        }

        Logger.debug{"Detaching vertex shader"}
        if (vertexShaderId != 0) {
            Logger.debug{"Successfully detached vertex shader"}
            GL20.glDetachShader(programId, vertexShaderId)
        }
        Logger.debug{"Detaching fragment shader"}
        if (fragmentShaderId != 0) {
            Logger.debug{"Successfully detached fragment shader"}
            GL20.glDetachShader(programId, fragmentShaderId)
        }

        Logger.debug{"Validating shader code"}
        GL20.glValidateProgram(programId)
        if (GL20.glGetProgrami(programId, GL20.GL_VALIDATE_STATUS) == 0) {
            Logger.warn{"Warning validating Shader code: " + GL20.glGetProgramInfoLog(programId, 1024)}
        }
    }

    fun bind() {
        GL20.glUseProgram(programId)
    }

    fun unbind() {
        GL20.glUseProgram(0)
    }

    fun cleanup() {
        unbind()
        if (programId != 0) {
            GL20.glDeleteProgram(programId)
        }
    }
}