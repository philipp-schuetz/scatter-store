package com.philippschuetz

import com.philippschuetz.configuration.*
import com.philippschuetz.encryption.EncryptionAES
import com.philippschuetz.providers.ProviderFTP
import com.philippschuetz.splitting.splitFile
import picocli.CommandLine
import java.nio.file.Path
import java.util.concurrent.Callable
import kotlin.io.path.createFile
import kotlin.io.path.isDirectory
import kotlin.io.path.isRegularFile
import kotlin.io.path.name
import kotlin.system.exitProcess


@CommandLine.Command(
    name = "scatter-store", mixinStandardHelpOptions = true, version = ["scatter-store 1.0"],
    description = ["Encrypts, splits and uploads files to different cloud storage providers."]
)
class ScatterStore : Callable<Int> {

    @CommandLine.Option(names = ["--init"], description = ["Create config and Database."])
    private var initialize: Boolean = false

    @CommandLine.Option(names = ["--upload"], description = ["Upload a file."])
    private lateinit var file: Path

    override fun call(): Int {
        if (initialize) { // --init
            // create an empty config file with an AES Key, create a database file and add db structure
            initConfig()
            EncryptionAES().generateKey()
            getDBPath().createFile()
            DB().init()
        } else if (this::file.isInitialized) { // --upload
            val files: MutableList<Path> = mutableListOf()
            if (file.isDirectory()) {
                files.addAll(listAllFiles(file))
            } else if (file.isRegularFile()) {
                files.add(file)
            } else
                return 1
            val db = DB()
            val encryptionNumber: Int = 0
            val numberOfShards: Int = 2

            val encryptionId = getEncryptionId(encryptionNumber)

            for (file in files) {
                var generatedFileId: String
                do {
                    generatedFileId = getRandomString(8)
                } while (db.fileIdInUse(generatedFileId))
                val fileObj = FileTypeDB(generatedFileId, file.name, getProviderIds(0, numberOfShards), encryptionId)
                //encrypt
                when (getEncryptionAlgorithm(encryptionNumber)) {
                    EncryptionType.AES -> EncryptionAES().encryptFiles(listOf(file), encryptionNumber)
                }
                //split
                val fileShards = splitFile(file, numberOfShards, generatedFileId)

                //upload
                val providerTypes = getProviderTypes(0, numberOfShards)
                for (provider in providerTypes) {
                    when (provider) {
                        ProviderType.FTP -> ProviderFTP()
                    }
                }



                // on success: add file to db
                db.addFile(fileObj)
            }
        }
        return 0
    }
}

fun main(args: Array<String>): Unit = exitProcess(CommandLine(ScatterStore()).execute(*args))