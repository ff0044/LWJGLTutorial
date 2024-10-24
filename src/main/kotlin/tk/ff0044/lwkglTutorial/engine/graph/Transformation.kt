package tk.ff0044.lwkglTutorial.engine.graph

import org.joml.Matrix4f
import org.joml.Vector3f
import tk.ff0044.lwkglTutorial.engine.GameItem

class Transformation {
    private val projectionMatrix = Matrix4f()
    private val viewMatrix = Matrix4f()
    private val modelViewMatrix = Matrix4f()

    fun getProjectionMatrix(fov: Float, width: Float, height: Float, zNear: Float, zFar: Float): Matrix4f {
        val aspectRatio = width / height
        projectionMatrix.identity()
        projectionMatrix.perspective(fov, aspectRatio, zNear, zFar)
        return projectionMatrix
    }

    fun getModelViewMatrix(gameItem: GameItem, viewMatrix: Matrix4f): Matrix4f {
        val rotation = gameItem.rotation
        modelViewMatrix.set(viewMatrix.translate(gameItem.position))
            .rotateX(Math.toRadians(-rotation.x.toDouble()).toFloat())
            .rotateX(Math.toRadians(-rotation.y.toDouble()).toFloat())
            .rotateZ(Math.toRadians(-rotation.z.toDouble()).toFloat())
            .scale(gameItem.scale)
        return modelViewMatrix
    }

    fun getViewMatrix(camera: Camera) : Matrix4f {
        val cameraPos = camera.position
        val rotation = camera.rotation

        viewMatrix.identity()
        // First do the rotation so camera rotates over its position
        viewMatrix.rotate(Math.toRadians(rotation.x.toDouble()).toFloat(), Vector3f(1f,0f,0f))
            .rotate(Math.toRadians(rotation.y.toDouble()).toFloat(), Vector3f(0f,1f,0f))
        // Then do the translation
        viewMatrix.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z)
        return viewMatrix
    }
}