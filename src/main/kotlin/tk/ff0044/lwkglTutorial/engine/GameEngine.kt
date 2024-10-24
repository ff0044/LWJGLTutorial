package tk.ff0044.lwkglTutorial.engine

import org.tinylog.Logger


open class GameEngine(
    windowTitle: String?,
    width: Int,
    height: Int,
    vSync: Boolean,
    private var gameLogic: IGameLogic
) : Runnable {

    val TARGET_FPS : Int = 75;
    val TARGET_UPS : Int = 30;

    private val window: Window
    private val timer: Timer
    private val mouseInput : MouseInput

    init {
        Logger.debug{"Initialising Game Engine"}
        try {
            Logger.debug{"Window is initialised with " +
                    "$windowTitle, " +
                    "$width, $height" +
                    "vSync is $vSync"
            }
            window = Window(windowTitle, width, height, vSync)
            timer = Timer()
            mouseInput = MouseInput()
        } catch (e: Exception) {
            throw Exception("Initialisation failed", e)
        }
    }

    override fun run() {
        try {
            init();
            gameLoop();
        } catch (e : Exception) {
            Logger.error("An error occured while attempting to run GameEngine classs: " +
                    "${e.printStackTrace()}", e.printStackTrace())
        } finally {
            cleanup()
        }
    }

    @Throws(Exception::class)
    protected fun init() {
        Logger.debug{"Window is initialised"}
        window.init();
        Logger.debug{"Timer is initialised"}
        timer.init();
        Logger.debug{"Mouse Input is initialised"}
        mouseInput.init(window)
        Logger.debug{"Game Logic is initialised"}
        gameLogic.init(window);
    }

    fun gameLoop() {
        var elapsedTime: Float
        var accumulator = 0f
        val interval = 1f / TARGET_UPS

        val running = true
        while (running && !window.windowShouldClose()!!) {
            elapsedTime = timer.getElapsedTime()
            accumulator += elapsedTime

            input()

            while (accumulator >= interval) {
                update(interval)
                accumulator -= interval
            }

            render()

            if (window.isvSync() == false) {
                sync()
            }
        }
    }

    private fun sync() {
        val loopSlot = 1f / TARGET_FPS
        val endTime = timer.getLastLoopTime() + loopSlot
        while (timer.getTime() < endTime) {
            try {
                Thread.sleep(1)
            } catch (ie: InterruptedException) {
            }
        }
    }

    fun input() {
        gameLogic.input(window, mouseInput)
    }

    fun update(interval: Float) {
        gameLogic.update(interval, mouseInput)
    }

    fun render() {
        gameLogic.render(window)
        window.update()
    }

    fun cleanup() {
        Logger.debug{"gameLogic is now being cleaned up"}
        gameLogic.cleanup()
    }

}