package tk.ff0044.lwkglTutorial.engine

class FileError : Exception {
    constructor() : super()
    constructor(message: String) : super("An error has occurred with reading the file. " +
            "Either there is an IOException(), FileNotFoundException() or NullPointerException(): \n${message}\n")
    constructor(message: String, cause: Throwable) : super(message, cause)
    constructor(cause: Throwable) : super(cause)
}

