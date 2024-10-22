package tk.ff0044.lwkglTutorial.engine

import org.joml.Vector3f
import tk.ff0044.lwkglTutorial.engine.graph.Mesh


class GameItem (private var mesh : Mesh){
    private var position : Vector3f = Vector3f()
    private var scale : Float = 1f
    private var rotation : Vector3f = Vector3f()

    fun getPosition(): Vector3f = position

    fun setPosition(x: Float, y: Float, z: Float) {
        position.set(x,y,z)
    }

    fun getScale(): Float = scale

    fun setScale(scale: Float) {
        this.scale = scale
    }

    fun getRotation(): Vector3f = rotation

    fun setRotation(x: Float, y: Float, z: Float) {
        rotation.set(x, y, z)
    }

    fun getMesh(): Mesh {return mesh}
}