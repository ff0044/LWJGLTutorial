package tk.ff0044.lwkglTutorial.engine

interface IGameLogic {
    @Throws(Exception::class)
    fun init()
    fun input(window: Window?)
    fun update(interval: Float)
    fun render(window: Window?)
    fun cleanup()
}