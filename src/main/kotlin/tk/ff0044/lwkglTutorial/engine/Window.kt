package tk.ff0044.lwkglTutorial.engine

import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.glfw.GLFWVidMode
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*
import org.lwjgl.system.MemoryUtil.NULL
import org.tinylog.Logger


class Window(private val title: String?, private var width: Int, private var height: Int, private var vSync: Boolean) {
    private var windowHandle: Long? = null
    private var resized = false

    fun init() {
        // Sets up an error callback. It will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set()

        // Initialise GLFW
        Logger.debug { "Initialising GLFW" }
        if (!glfwInit()) {
            Logger.error("Unable to initialise GLFW", IllegalStateException())
            throw IllegalStateException()
        }

        Logger.debug { "Using default window hints" }
        glfwDefaultWindowHints()
        Logger.debug { "GLFW_VISIBLE is false" }
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE)
        Logger.debug { "GLFW_RESIZABLE is true" }
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE)
        Logger.debug { "GLFW_CONTEXT_VERSION is 3.3" }
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3)
        Logger.debug { "GLFW_OPENGL_PROFILE is core" }
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)
        Logger.debug { "GLFW_OPENGL_FORWARD_COMPAT is true" }
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE)

        // Create the window
        Logger.debug { "Creating GLFW Window" }
        windowHandle = glfwCreateWindow(width, height, title, NULL, NULL)
        if (windowHandle == NULL) {
            Logger.error("Failed to create the GLFW Window", RuntimeException())
            throw RuntimeException()
        }

        // Sets up resize callback
        Logger.debug { "Setting up the resize callback" }
        glfwSetFramebufferSizeCallback(windowHandle!!) { window: Long, width: Int, height: Int ->
            this.width = width
            this.height = height
            this.setResized(true)
        }


        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        Logger.debug{"Setting up the key callback"}
        glfwSetKeyCallback(
            windowHandle!!
        ) { window: Long, key: Int, scancode: Int, action: Int, mods: Int ->
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                Logger.debug{"GLFW_KEY_ESCAPE has been pressed. Window will close now. "}
                glfwSetWindowShouldClose(window, true) // We will detect this in the rendering loop
            }
        }

        Logger.debug { "Centering the window" }
        // Center the window
        val vidMode: GLFWVidMode? = glfwGetVideoMode(glfwGetPrimaryMonitor())
        if (vidMode != null) {
            glfwSetWindowPos(
                windowHandle!!,
                (vidMode.width() - width) / 2,
                (vidMode.height() - height) / 2
            )
        }

        glfwMakeContextCurrent(windowHandle!!)
        GL.createCapabilities()

        Logger.debug { "Cleared color to 0,0,0,0 (rgba)" }
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f)

        Logger.debug { "Showing the window" }
        glfwShowWindow(windowHandle!!)

    }

    fun setClearColor(red: Float, green: Float, blue: Float, alpha: Float) {
        glClearColor(red, green, blue, alpha)
    }

    fun isKeyPressed(keyCode: Int): Boolean {
        return windowHandle?.let { glfwGetKey(it, keyCode) } == GLFW_PRESS
    }

    fun windowShouldClose(): Boolean? {
        return windowHandle?.let { glfwWindowShouldClose(it) }
    }

    fun getTitle(): String {
        return title!!
    }

    fun getWidth(): Int {
        return width
    }

    fun getHeight(): Int {
        return height
    }

    fun isResized(): Boolean {
        return resized
    }

    fun setResized(resized: Boolean) {
        this.resized = resized
    }

    fun isvSync(): Boolean {
        return vSync
    }

    fun setvSync(vSync: Boolean) {
        this.vSync = vSync
    }

    fun update() {
        windowHandle?.let { glfwSwapBuffers(it) }
        glfwPollEvents()
    }
}