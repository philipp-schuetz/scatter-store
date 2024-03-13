package com.philippschuetz.encryption

import com.philippschuetz.EncryptionType
import com.philippschuetz.configuration.addEncryptionKey
import com.philippschuetz.configuration.getEncryptionKey
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.nio.file.Path
import java.security.SecureRandom
import java.util.*
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import kotlin.io.path.inputStream
import kotlin.io.path.outputStream
import kotlin.io.path.readBytes

class EncryptionAES {

    companion object {
        fun generateKey() {
            val secureRandom = SecureRandom()
            val keyGenerator = KeyGenerator.getInstance("AES")
            keyGenerator?.init(256, secureRandom)
            val secretKey = keyGenerator?.generateKey()

            val encodedKey: String = Base64.getEncoder().encodeToString(secretKey!!.encoded)
            addEncryptionKey(EncryptionType.AES, encodedKey)
        }
    }

    private fun getSecretKey(keyIndex: Int): SecretKey {
        return SecretKeySpec(Base64.getDecoder().decode(getEncryptionKey(keyIndex, EncryptionType.AES)), "AES")
    }

    private fun saveFile(fileData: ByteArray, filePath: Path) {
        val bos = BufferedOutputStream(filePath.outputStream())
        bos.write(fileData)
        bos.flush()
        bos.close()
    }

    private fun readFile(filePath: Path): ByteArray {
        val fileContents = filePath.readBytes()
        val inputBuffer = BufferedInputStream(filePath.inputStream())

        inputBuffer.read(fileContents)
        inputBuffer.close()

        return fileContents
    }

    fun encryptFiles(inputFiles: List<Path>, keyIndex: Int) {
        for (filePath in inputFiles) {
            val fileData = readFile(filePath)
            val secretKey = getSecretKey(keyIndex)

            val data = secretKey.encoded
            val secretKeySpec = SecretKeySpec(data, 0, data.size, "AES")
            val cipher = Cipher.getInstance("AES", "BC")
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, IvParameterSpec(ByteArray(cipher.blockSize)))
            val encodedData = cipher.doFinal(fileData)

            saveFile(encodedData, filePath)
        }
    }

    fun decryptFiles(inputFiles: List<Path>, keyIndex: Int) {
        for (filePath in inputFiles) {
            val fileData = readFile(filePath)
            val secretKey = getSecretKey(keyIndex)

            val decrypted: ByteArray
            val cipher = Cipher.getInstance("AES", "BC")
            cipher.init(Cipher.DECRYPT_MODE, secretKey, IvParameterSpec(ByteArray(cipher.blockSize)))
            decrypted = cipher.doFinal(fileData)

            saveFile(decrypted, filePath)
        }
    }
}