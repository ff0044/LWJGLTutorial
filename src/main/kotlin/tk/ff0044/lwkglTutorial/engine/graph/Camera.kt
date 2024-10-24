package tk.ff0044.lwkglTutorial.engine.graph

import org.joml.Vector3f
import kotlin.math.cos
import kotlin.math.sin

class Camera {
    private lateinit var position : Vector3f
    private lateinit var rotation : Vector3f

    constructor() {
        position = Vector3f()
        rotation = Vector3f()
    }

    constructor(position : Vector3f, rotation : Vector3f) {
        this.position = position
        this.rotation = rotation
    }

    fun getPosition() : Vector3f {
        return position
    }

    fun setPosition(x : Float, y : Float, z : Float) {
        position.x = x
        position.y = y
        position.z = z
    }

    fun movePosition(offsetX : Float, offsetY : Float, offsetZ:Float) {
        if (offsetZ != 0f) {
            position.x += sin(Math.toRadians(rotation.y.toDouble()).toFloat()) * -1.0f * offsetZ
            position.z += cos(Math.toRadians(rotation.y.toDouble()).toFloat()) * offsetZ
        }

        if (offsetX != 0f) {
            position.x += sin(Math.toRadians((rotation.y-90).toDouble()).toFloat()) * -1.0f * offsetX
            position.z += cos(Math.toRadians((rotation.y-90).toDouble()).toFloat()) * offsetX
        }

        position.y += offsetY
    }

    fun getRotation(): Vector3f {
        return rotation
    }

    fun setRotation(x : Float, y : Float, z : Float) {
        rotation.x = x;
        rotation.y = y;
        rotation.z = z;
    }

    fun moveRotation(offsetX : Float, offsetY : Float, offsetZ:Float) {
        rotation.x += offsetX;
        rotation.y += offsetY;
        rotation.z += offsetZ;
    }
}