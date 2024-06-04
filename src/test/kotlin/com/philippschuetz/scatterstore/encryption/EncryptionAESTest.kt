package com.philippschuetz.scatterstore.encryption

import com.philippschuetz.scatterstore.areFilesIdentical
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import kotlin.io.path.Path
import kotlin.io.path.deleteExisting
import kotlin.io.path.exists

class EncryptionAESTest {

    private val encryptionAES = EncryptionAES("Ejj7B+hWqI9R8trUXlMdty7N1TPuQ2YDZmza6CmhhQ4=")
    private val encryptionResourcesBasePath = "src/test/resources/encryption/"
    private val toEncrypt = listOf(
        Path(encryptionResourcesBasePath + "to-encrypt-1.txt"),
        Path(encryptionResourcesBasePath + "to-encrypt-2.txt")
    )
    private val encrypted = listOf(
        Path(encryptionResourcesBasePath + "encrypted-1.txt"),
        Path(encryptionResourcesBasePath + "encrypted-2.txt")
    )
    private val toDecrypt = listOf(
        Path(encryptionResourcesBasePath + "to-decrypt-1.txt"),
        Path(encryptionResourcesBasePath + "to-decrypt-2.txt")
    )
    private val decrypted = listOf(
        Path(encryptionResourcesBasePath + "decrypted-1.txt"),
        Path(encryptionResourcesBasePath + "decrypted-2.txt")
    )

    @BeforeEach
    fun setUp() {

    }

    @Test
    fun generateKey() {
    }

    @Test
    fun getSecretKey() {

    }

    @Test
    fun encryptFiles() {
        encryptionAES.encryptFiles(toEncrypt, encrypted)

        for (i in toEncrypt.indices) {
            assertTrue(areFilesIdentical(encrypted[i], toDecrypt[i]))
        }
    }


    @Test
    fun decryptFiles() {
        encryptionAES.decryptFiles(toDecrypt, decrypted)

        for (i in toDecrypt.indices) {
            assertTrue(areFilesIdentical(decrypted[i], toEncrypt[i]))
        }
    }


    @AfterEach
    fun tearDown() {
        for (encryptedFile in encrypted) {
            if (encryptedFile.exists()) {
                encryptedFile.deleteExisting()
            }
        }
        for (decryptedFile in decrypted) {
            if (decryptedFile.exists()) {
                decryptedFile.deleteExisting()
            }
        }
    }
}