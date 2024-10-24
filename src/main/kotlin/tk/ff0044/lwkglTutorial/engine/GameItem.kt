package tk.ff0044.lwkglTutorial.engine

import org.joml.Vector3f
import tk.ff0044.lwkglTutorial.engine.graph.Mesh

class GameItem(var mesh: Mesh) {
    var position: Vector3f = Vector3f(0f, 0f, 0f)
    var scale: Float = 1f
    var rotation: Vector3f = Vector3f(0f, 0f, 0f)

    fun setPosition(x: Float, y: Float, z: Float) {
        position.x = x
        position.y = y
        position.z = z
    }

    fun setRotation(x: Float, y: Float, z: Float) {
        rotation.x = x
        rotation.y = y
        rotation.z = z
    }
}