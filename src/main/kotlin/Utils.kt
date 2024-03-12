package com.philippschuetz

import java.nio.file.Path
import java.util.*
import kotlin.io.path.Path


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
    return Path(System.getProperty("java.io.tmpdir"))
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