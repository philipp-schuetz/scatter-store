package com.philippschuetz

import java.nio.file.Path
import java.util.*
import kotlin.io.path.Path


fun getOperatingSystem(): String {
    return System.getProperty("os.name").lowercase(Locale.getDefault())
}

fun getTmpFolder(): Path {
    return Path(System.getProperty("java.io.tmpdir"))
}
