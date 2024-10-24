package tk.ff0044.lwkglTutorial.engine

interface IGameLogic {

    @Throws(Exception::class)
    fun init(window : Window)

    fun input(window: Window, mouseInput: MouseInput)
    fun update(interval: Float, mouseInput: MouseInput)
    fun render(window: Window)
    fun cleanup()
}