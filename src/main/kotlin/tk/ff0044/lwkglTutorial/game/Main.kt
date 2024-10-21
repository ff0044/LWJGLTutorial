package tk.ff0044.lwkglTutorial.game

import tk.ff0044.lwkglTutorial.engine.GameEngine
import org.tinylog.Logger
import org.tinylog.configuration.Configuration
import tk.ff0044.lwkglTutorial.engine.IGameLogic
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    var title = "LWJGL Kotlin Tutorial"
    var width = 1024
    var height = 786
    var vSync = true
    var isDevMode = false

    for (arg in args) {
        when {
            arg.startsWith("--title=") -> {
                title = arg.substringAfter("=")
                Logger.info { "Title set to: $title" }
            }
            arg.startsWith("--windowDimensions=") -> {
                val dimensions = arg.substringAfter("=").split(",")
                if (dimensions.size == 2) {
                    try {
                        width = dimensions[0].toInt()
                        height = dimensions[1].toInt()
                        Logger.info { "Window dimensions set to: ${width}x${height}" }
                    } catch (e: NumberFormatException) {
                        Logger.error { "Invalid window dimensions format. Using default: ${width}x${height}" }
                    }
                } else {
                    Logger.error { "Invalid window dimensions format. Using default: ${width}x${height}" }
                }
            }
            arg.startsWith("--vsync=") -> {
                vSync = when (arg.substringAfter("=")) {
                    "true" -> true
                    "false" -> false
                    else -> {
                        Logger.error { "Invalid vSync value. Using default: $vSync" }
                        vSync
                    }
                }
                Logger.info { "vSync set to: $vSync" }
            }
            arg == "--dev" -> {
                isDevMode = true
                title += " [DEV]"
                Configuration.set("writer1.level", "debug")
                Logger.info { "Developer mode enabled, watch out 👻👻👻" }
            }
        }
    }

    if (!isDevMode) {
        Configuration.set("writer1.level", "info")
    }

    try {
        Logger.info { "vSync is $vSync" }
        val gameLogic: IGameLogic = DummyGame()
        val gameEng: GameEngine = GameEngine(title, width, height, vSync, gameLogic)
        Logger.info { "Running game engine" }
        gameEng.run()
    } catch (e: Exception) {
        Logger.error("Error while running game: ${e.printStackTrace()}", e)
        exitProcess(-1)
    }
}