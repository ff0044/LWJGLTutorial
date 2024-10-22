package tk.ff0044.lwkglTutorial.engine.graph

import org.joml.Matrix4f
import org.joml.Vector3f

class Transformation {
    private var projectionMatrix: Matrix4f = Matrix4f()

    private var worldMatrix: Matrix4f = Matrix4f()

    fun Transformation() {
        worldMatrix = Matrix4f()
        projectionMatrix = Matrix4f()
    }

    fun getProjectionMatrix(fov: Float, width: Float, height: Float, zNear: Float, zFar: Float): Matrix4f {
        return projectionMatrix.setPerspective(fov, width / height, zNear, zFar)
    }

    fun getWorldMatrix(offset: Vector3f, rotation: Vector3f, scale: Float): Matrix4f {
        return worldMatrix.translation(offset)
            .rotateX((Math.toRadians(rotation.x.toDouble()).toFloat()))
            .rotateY((Math.toRadians(rotation.y.toDouble()).toFloat()))
            .rotateZ((Math.toRadians(rotation.z.toDouble()).toFloat()))
            .scale(scale)
    }
}