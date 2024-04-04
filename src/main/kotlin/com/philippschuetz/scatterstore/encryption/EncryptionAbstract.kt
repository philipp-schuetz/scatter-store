package com.philippschuetz.scatterstore.encryption

import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.nio.file.Path

import javax.crypto.SecretKey
import kotlin.io.path.inputStream
import kotlin.io.path.outputStream
import kotlin.io.path.readBytes

abstract class EncryptionAbstract {
    /**
     * Generates a key for encryption and writes it to the config file.
     */
    abstract fun generateKey()

    /**
     * Saves the provided file data to the specified file path.
     * @param fileData The data to be saved to the file.
     * @param filePath The path where the file should be saved.
     */
    fun saveFile(fileData: ByteArray, filePath: Path) {
        val bos = BufferedOutputStream(filePath.outputStream())
        bos.write(fileData)
        bos.flush()
        bos.close()
    }

    /**
     * Reads the file at the specified path and returns its data.
     * @param filePath The path of the file to read.
     * @return The data of the file as a byte array.
     */
    fun readFile(filePath: Path): ByteArray {
        val fileContents = filePath.readBytes()
        val inputBuffer = BufferedInputStream(filePath.inputStream())

        inputBuffer.read(fileContents)
        inputBuffer.close()

        return fileContents
    }

    /**
     * Encrypts the files at the provided paths using the key at the specified index.
     * @param inputFiles The paths of the files to encrypt.
     * @param outputPaths Define output paths for encrypted files.
     */
    abstract fun encryptFiles(inputFiles: List<Path>, outputPaths: List<Path>)

    /**
     * Decrypts the files at the provided paths using the key at the specified index.
     * @param inputFiles The paths of the files to decrypt.
     * @param outputPaths Define output paths for encrypted files.
     */
    abstract fun decryptFiles(inputFiles: List<Path>, outputPaths: List<Path>)
}