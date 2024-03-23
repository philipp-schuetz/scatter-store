package scatterstore.splitting

import scatterstore.getTmpFolder
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
fun joinFiles(fileId: String, numberOfFiles: Int): Path {
    val outputFile = Path("output.json") // TODO lookup filename from config/db
    val inputFiles: MutableList<Path> = ArrayList()
    for (i in 1..numberOfFiles) {
        inputFiles.add(Path("${getTmpFolder()}/$fileId-$i"))
    }
    outputFile.outputStream().use { out ->
        inputFiles.forEach { inputFile ->
            inputFile.inputStream().use { it.copyTo(out) }
        }
    }
    return outputFile
}