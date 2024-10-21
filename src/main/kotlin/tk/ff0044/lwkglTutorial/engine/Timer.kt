package tk.ff0044.lwkglTutorial.engine

class Timer {
    private var lastLoopTime : Double = 0.0

    fun init() {
        lastLoopTime = getTime()
    }

    fun getTime(): Double {
        return System.nanoTime() / 1000000000.0
    }

    fun getElapsedTime(): Float {
        val time = getTime()
        val elapsedTime = (time - lastLoopTime).toFloat()
        lastLoopTime = time
        return elapsedTime
    }

    fun getLastLoopTime(): Double {
        return lastLoopTime
    }

}