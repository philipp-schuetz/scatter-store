package scatterstore.encryption

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
     * Retrieves the encryption key from config and converts it to.
     * @param keyIndex The index in the config file of the key to retrieve.
     * @return The secret key corresponding to the provided index.
     */
    abstract fun getSecretKey(keyIndex: Int): SecretKey


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
     * @param keyIndex The index in the config file of the key to retrieve.
     */
    abstract fun encryptFiles(inputFiles: List<Path>, keyIndex: Int)

    /**
     * Decrypts the files at the provided paths using the key at the specified index.
     * @param inputFiles The paths of the files to decrypt.
     * @param keyIndex The index in the config file of the key to retrieve.
     */
    abstract fun decryptFiles(inputFiles: List<Path>, keyIndex: Int)
}