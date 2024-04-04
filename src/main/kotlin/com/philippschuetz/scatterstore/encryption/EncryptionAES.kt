package com.philippschuetz.scatterstore.encryption

import com.philippschuetz.scatterstore.EncryptionType
import com.philippschuetz.scatterstore.configuration.addEncryptionKey
import java.nio.file.Path
import java.security.SecureRandom
import java.util.*
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

class EncryptionAES(encryptionKey: String) : EncryptionAbstract() {

    private val secretKey: SecretKey = SecretKeySpec(Base64.getDecoder().decode(encryptionKey), "AES")

    override fun generateKey() {
        val secureRandom = SecureRandom()
        val keyGenerator = KeyGenerator.getInstance("AES")
        keyGenerator?.init(256, secureRandom)
        val secretKey = keyGenerator?.generateKey()

        val encodedKey: String = Base64.getEncoder().encodeToString(secretKey!!.encoded)
        addEncryptionKey(EncryptionType.AES, encodedKey)
    }

    override fun encryptFiles(inputFiles: List<Path>, outputPaths: List<Path>) {
        if (inputFiles.size != outputPaths.size) throw Exception("same length required for input and output files")
        for (i in 0 .. inputFiles.size) {
            val fileData = readFile(inputFiles[i])

            val data = secretKey.encoded
            val secretKeySpec = SecretKeySpec(data, 0, data.size, "AES")
            val cipher = Cipher.getInstance("AES")
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec)
            val encodedData = cipher.doFinal(fileData)

            saveFile(encodedData, outputPaths[i])
        }
    }

    override fun decryptFiles(inputFiles: List<Path>, outputPaths: List<Path>) {
        if (inputFiles.size != outputPaths.size) throw Exception("same length required for input and output files")
        for (i in 0 .. inputFiles.size) {
            val fileData = readFile(inputFiles[i])

            val decrypted: ByteArray
            val cipher = Cipher.getInstance("AES")
            cipher.init(Cipher.DECRYPT_MODE, secretKey)
            decrypted = cipher.doFinal(fileData)

            saveFile(decrypted, outputPaths[i])
        }
    }
}