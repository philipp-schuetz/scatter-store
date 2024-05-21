package com.philippschuetz.scatterstore

import java.nio.file.Files
import java.nio.file.Path
import java.util.*
import kotlin.io.path.Path
import kotlin.io.path.createDirectory
import kotlin.io.path.notExists


/**
 * Get the name of the current operating system.
 * @return lowercase string (e.g. "linux", "windows 10")
 */
fun getOperatingSystem(): String {
    return System.getProperty("os.name").lowercase(Locale.getDefault())
}

/**
 * Get the path to the folder used for temporary files. Path is based on the current operating system.
 * @return path to tmp folder
 */
fun getTmpFolder(): Path {
    val tmpDir = Path("${System.getProperty("java.io.tmpdir")}/scatter-store")
    if (tmpDir.notExists())
        tmpDir.createDirectory()
    return tmpDir
}

/**
 * Get the directory to use for storage of persistent data. Supports multiple operating systems.
 */
fun getDataFolder(): Path {
    return Path(".") // TODO
}

/**
 * Get the path to use for the config file. Supports multiple operating systems.
 */
fun getConfigPath(): Path {
    return Path("${getDataFolder()}/scatter-store.json")
}

/**
 * Get the path to use for the database file. Supports multiple operating systems.
 */
fun getDBPath(): Path {
    return Path("${getDataFolder()}/scatter-store.sqlite")
}

/**
 * Get a random String from a-zA-Z0-9.
 * @param length Specify the length of the generated String.
 */
fun getRandomString(length: Int): String {
    val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    return (1..length)
        .map { kotlin.random.Random.nextInt(0, charPool.size) }
        .map(charPool::get)
        .joinToString("")
}

/**
 * This function takes a directory path as input and returns a list of all files within that directory and its subdirectories.
 *
 * @param dir The path of the directory to be scanned.
 * @return A list of paths of all files within the specified directory and its subdirectories.
 */
fun listAllFiles(dir: Path): List<Path> {
    return Files.walk(dir)
        .filter { Files.isRegularFile(it) }
        .toList()
}