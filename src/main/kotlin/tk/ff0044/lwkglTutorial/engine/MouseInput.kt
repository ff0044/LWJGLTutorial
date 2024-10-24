package tk.ff0044.lwkglTutorial.engine

import org.joml.Vector2d
import org.joml.Vector2f
import org.lwjgl.glfw.GLFW.*
import java.awt.SystemColor


class MouseInput {
    private val previousPos = Vector2d(-1.0, -1.0)
    private val currentPos = Vector2d(0.0, 0.0)
    private val displVec = Vector2f()

    private var inWindow = false
    private var leftButtonPressed = false
    private var rightButtonPressed = false

    fun init(window : Window) {
        glfwSetCursorPosCallback(
            window.getWindowHandle()
        ) { windowHandle: Long, xpos: Double, ypos: Double ->
            currentPos.x = xpos
            currentPos.y = ypos
        }
        glfwSetCursorEnterCallback(
            window.getWindowHandle()
        ) { windowHandle: Long, entered: Boolean ->
            inWindow = entered
        }
        glfwSetMouseButtonCallback(
            window.getWindowHandle()
        ) { windowHandle: Long, button: Int, action: Int, mode: Int ->
            leftButtonPressed = button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS
            rightButtonPressed = button == GLFW_MOUSE_BUTTON_2 && action == GLFW_PRESS
        }
    }

    fun getDisplVec(): Vector2f {
        return displVec
    }

    fun input(window:Window) {
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

    fun isLeftButtonPressed() : Boolean {
        return leftButtonPressed
    }

    fun isRightButtonPressed() : Boolean {
        return rightButtonPressed
    }
}