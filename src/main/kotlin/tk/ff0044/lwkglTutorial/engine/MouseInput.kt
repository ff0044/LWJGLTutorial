package tk.ff0044.lwkglTutorial.engine

import org.joml.Vector2d
import org.joml.Vector2f
import org.lwjgl.glfw.GLFW

class MouseInput {
    private val previousPos = Vector2d(-1.0, -1.0)

    private val currentPos = Vector2d(0.0, 0.0)

    val displVec: Vector2f = Vector2f()

    private var inWindow = false

    var isLeftButtonPressed: Boolean = false
        private set

    var isRightButtonPressed: Boolean = false
        private set

    fun init(window: Window) {
        GLFW.glfwSetCursorPosCallback(window.getWindowHandle()) { windowHandle: Long, xpos: Double, ypos: Double ->
            currentPos.x = xpos
            currentPos.y = ypos
        }
        GLFW.glfwSetCursorEnterCallback(window.getWindowHandle()) { windowHandle: Long, entered: Boolean ->
            inWindow = entered
        }
        GLFW.glfwSetMouseButtonCallback(window.getWindowHandle()) { windowHandle: Long, button: Int, action: Int, mode: Int ->
            isLeftButtonPressed = button == GLFW.GLFW_MOUSE_BUTTON_1 && action == GLFW.GLFW_PRESS
            isRightButtonPressed = button == GLFW.GLFW_MOUSE_BUTTON_2 && action == GLFW.GLFW_PRESS
        }
    }

    fun input(window: Window?) {
        displVec.x = 0f
        displVec.y = 0f
        if (previousPos.x > 0 && previousPos.y > 0 && inWindow) {
            val deltax : Double = currentPos.x - previousPos.x
            val deltay : Double = currentPos.y - previousPos.y
            val rotateX : Boolean = deltax != 0.0
            val rotateY : Boolean = deltay != 0.0
            if (rotateX) {
                displVec.y = deltax.toFloat()
            }
            if (rotateY) {
                displVec.x = deltay.toFloat()
            }
        }
        previousPos.x = currentPos.x
        previousPos.y = currentPos.y
    }
}
