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
            arg.startsWith("--dev") -> {
                isDevMode = true
                title += " [DEV]"
                Configuration.set("writer1.level", "debug")
                Logger.info {"""
                    
                    -------------------------------------- START OF NEW LOG --------------------------------------
                    
                     *=====================================================*
                    /   --dev has been enabled in the program arguments     \
                    |                                                       |
                    |       To disable it, quit this app, remove the        |
                    |               argument, then rerun it.                |
                    |                                                       |
                    |    If not removed, nothing bad will happen, however   |
                    |    performance will be heavily degraded from the      |
                    |              debugging of the dev mode.               |
                    |                                                       |
                    |           If you are intending to do this:            |
                    |       Developer mode has been enabled, watch out      |
                     \                  ○( ＾皿＾)っ Hehehe…                 /
                     *=====================================================*
                    
                    
                    
                """.trimIndent()}
            }
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

        }
    }

    if (!isDevMode) {
        Configuration.set("writer1.level", "info")
    }

    try {
        if (!isDevMode) {Logger.info{"\n-------------------------------------- START OF NEW LOG --------------------------------------"}}
        Logger.info { "vSync is $vSync" }
        val gameLogic: IGameLogic = DummyGame()
        val gameEng: GameEngine = GameEngine(title, width, height, vSync, gameLogic)
        Logger.info { "Running game engine" }
        gameEng.run()
    } catch (e: Exception) {
        Logger.error("Error while running game: ${e.message}", e.printStackTrace())
        exitProcess(-1)
    }
}