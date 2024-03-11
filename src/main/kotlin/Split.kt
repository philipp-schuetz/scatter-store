package com.philippschuetz

import java.nio.file.Path
import kotlin.io.path.fileSize
import kotlin.io.path.inputStream
import kotlin.io.path.outputStream


private fun splitByFileSize(inputFile: Path, maxSizeOfSplitFiles: Int, fileId: String): List<Path> {
    val listOfSplitFiles: MutableList<Path> = ArrayList()
    inputFile.inputStream().use { `in` ->
        val buffer = ByteArray(maxSizeOfSplitFiles)
        var dataRead = `in`.read(buffer)
        while (dataRead > -1) {
            val splitFile = getSplitFile(fileId, buffer, dataRead, listOfSplitFiles.size+1)
            listOfSplitFiles.add(splitFile)
            dataRead = `in`.read(buffer)
        }
    }
    return listOfSplitFiles
}
private fun getSplitFile(fileId: String, buffer: ByteArray, length: Int, fileNumber: Int): Path {
    val splitFileT = Path.of("./$fileId-$fileNumber")// directory: getTmpFolder()
    splitFileT.outputStream().use { fos ->
        fos.write(buffer, 0, length)
    }
    return splitFileT
}


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

fun splitFile(inputFile: Path, numberOfFiles: Int, fileId: String): List<Path> {
    return splitByFileSize(inputFile, getSizeInBytes(inputFile.fileSize(), numberOfFiles), fileId)
}

fun main() {
    splitFile(Path.of("test.json"), 2, "qwertz")
}