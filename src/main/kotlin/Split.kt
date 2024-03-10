package com.philippschuetz

import java.io.File
import java.io.FileOutputStream
import java.nio.file.Files


private fun splitByFileSize(inputFile: File, maxSizeOfSplitFiles: Int, fileId: String): List<File> {
    val listOfSplitFiles: MutableList<File> = ArrayList()
    Files.newInputStream(inputFile.toPath()).use { `in` ->
        val buffer = ByteArray(maxSizeOfSplitFiles)
        var dataRead = `in`.read(buffer)
        while (dataRead > -1) {
            val splitFile = getSplitFile(fileId, buffer, dataRead)
            listOfSplitFiles.add(splitFile)
            dataRead = `in`.read(buffer)
        }
    }
    return listOfSplitFiles
}
var fileNumber = 0
private fun getSplitFile(fileId: String, buffer: ByteArray, length: Int): File {
    fileNumber += 1
    println(fileNumber)
    val splitFile = File.createTempFile("$fileId-", "$fileNumber", File(".")) //getTmpFolder().toFile()
    FileOutputStream(splitFile).use { fos ->
        fos.write(buffer, 0, length)
    }
    return splitFile
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

fun splitFile(inputFile: File, numberOfFiles: Int, fileId: String): List<File> {
    return splitByFileSize(inputFile, getSizeInBytes(inputFile.length(), numberOfFiles), fileId)
}

fun main() {
    splitFile(File("test.json"), 2, "qwertz")
}