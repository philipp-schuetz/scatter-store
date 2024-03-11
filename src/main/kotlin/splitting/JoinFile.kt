package com.philippschuetz.splitting

import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.inputStream
import kotlin.io.path.outputStream

fun joinFiles(fileId: String, numberOfFiles: Int): Path {
    val outputFile = Path("output.json") // TODO lookup filename from config/db
    val inputFiles: MutableList<Path> = ArrayList()
    for (i in 1..numberOfFiles) {
        inputFiles.add(Path("$fileId-$i"))// TODO directory: getTmpFolder()
    }
    outputFile.outputStream().use { out ->
        inputFiles.forEach { inputFile ->
            inputFile.inputStream().use { it.copyTo(out) }
        }
    }
    return outputFile
}

fun main() {
    joinFiles("qwertz", 2)
}