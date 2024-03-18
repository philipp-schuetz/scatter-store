package com.philippschuetz.splitting

import com.philippschuetz.getTmpFolder
import java.nio.file.Path
import kotlin.io.path.fileSize
import kotlin.io.path.inputStream
import kotlin.io.path.outputStream
import kotlin.io.path.Path


/**
 * Splits the input file into multiple files based file size.
 * Output filenames: fileId-fileNumber
 *
 * @param inputFile The file to be split.
 * @param maxSizeOfSplitFiles The maximum combined size of the split files.
 * @param fileId The ID of the file to be split (used for naming).
 * @return A list of paths to the split files.
 */
private fun splitByFileSize(inputFile: Path, maxSizeOfSplitFiles: Int, fileId: String): List<Path> {
    val listOfSplitFiles: MutableList<Path> = ArrayList()
    inputFile.inputStream().use { `in` ->
        val buffer = ByteArray(maxSizeOfSplitFiles)
        var dataRead = `in`.read(buffer)
        while (dataRead > -1) {
            val splitFile = getSplitFile(fileId, buffer, dataRead, listOfSplitFiles.size + 1)
            listOfSplitFiles.add(splitFile)
            dataRead = `in`.read(buffer)
        }
    }
    return listOfSplitFiles
}

/**
 * Creates a split file with the given parameters.
 * Output filenames: fileId-fileNumber
 *
 * @param fileId The ID of the file to be split (used for naming).
 * @param buffer The buffer containing the data to be written to the split file.
 * @param length The length of the data to be written.
 * @param fileNumber The number of the split file (used for naming).
 * @return The path to the split file.
 */
private fun getSplitFile(fileId: String, buffer: ByteArray, length: Int, fileNumber: Int): Path {
    val splitFileT = Path("${getTmpFolder()}/$fileId-$fileNumber")
    splitFileT.outputStream().use { fos ->
        fos.write(buffer, 0, length)
    }
    return splitFileT
}


/**
 * Calculates the size in bytes for each split file.
 *
 * @param inputFileSizeInBytes The size of the input file in bytes.
 * @param numberOfFiles The number of files to split the input file into.
 * @return The size in bytes for each split file.
 * @throws NumberFormatException If the calculated size is larger than Int.MAX_VALUE.
 */
private fun getSizeInBytes(inputFileSizeInBytes: Long, numberOfFiles: Int): Int {
    var inputFileSizeInBytesVar = inputFileSizeInBytes
    if (inputFileSizeInBytesVar % numberOfFiles != 0L) {
        inputFileSizeInBytesVar = ((inputFileSizeInBytesVar / numberOfFiles) + 1) * numberOfFiles
    }
    val x = inputFileSizeInBytesVar / numberOfFiles
    if (x > Int.MAX_VALUE) {
        throw NumberFormatException("size too large")
    }
    return x.toInt()
}


/**
 * Splits the input file into a specified number of files.
 * Output filenames: fileId-fileNumber
 *
 * @param inputFile The file to be split.
 * @param numberOfFiles The number of files to split the input file into.
 * @param fileId The ID of the split files (used for naming the output files).
 * @return A list of paths to the split files.
 */
fun splitFile(inputFile: Path, numberOfFiles: Int, fileId: String): List<Path> {
    return splitByFileSize(inputFile, getSizeInBytes(inputFile.fileSize(), numberOfFiles), fileId)
}