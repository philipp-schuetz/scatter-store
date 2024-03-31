package com.philippschuetz.scatterstore.splitting

import com.philippschuetz.scatterstore.getTmpFolder
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.inputStream
import kotlin.io.path.outputStream

/**
 * Joins multiple files into a single file. When joining the original file name is restored from the database.
 *
 * @param fileId The unique identifier (before -number) of the files to be joined.
 * @param numberOfFiles The number of files to be joined.
 * @return The path of the output file.
 */
fun joinFiles(fileId: String, numberOfFiles: Int) {
    val inputFiles: MutableList<Path> = ArrayList()
    for (i in 1..numberOfFiles) {
        inputFiles.add(Path("${getTmpFolder()}/$fileId-$i"))
    }
    val outputPath = Path("${getTmpFolder()}/$fileId")
    outputPath.outputStream().use { out ->
        inputFiles.forEach { inputFile ->
            inputFile.inputStream().use { it.copyTo(out) }
        }
    }
}