package com.philippschuetz.scatterstore

import com.philippschuetz.scatterstore.configuration.*
import com.philippschuetz.scatterstore.encryption.EncryptionAES
import com.philippschuetz.scatterstore.encryption.EncryptionAbstract
import com.philippschuetz.scatterstore.splitting.splitFile
import picocli.CommandLine
import com.philippschuetz.scatterstore.providers.Provider
import com.philippschuetz.scatterstore.splitting.joinFiles
import java.nio.file.Path
import java.util.UUID
import java.util.concurrent.Callable
import kotlin.io.path.*
import kotlin.system.exitProcess


@CommandLine.Command(
    name = "scatter-store", mixinStandardHelpOptions = true, version = ["scatter-store 1.0"],
    description = ["Encrypts, splits and uploads files to different cloud storage providers."]
)
class ScatterStore : Callable<Int> {

    @CommandLine.Option(names = ["--init"], description = ["Create config and database."])
    private var initialize: Boolean = false

    @CommandLine.Option(
        names = ["--forceinit"],
        description = ["Same as --init but forces file creation (Warning: All stored application data is lost)"]
    )
    private var forceInitialize: Boolean = false

    @CommandLine.Option(names = ["--upload"], description = ["Upload a file. (Directory or File)"])
    private lateinit var fileUpload: Path

    @CommandLine.Option(names = ["--download"], description = ["Download a file. (File ID)"])
    private lateinit var fileIdDownload: String

    override fun call(): Int {
        if (initialize) { // --init
            // create an empty config file with an AES Key, create a database file and add db structure
            if (!getConfigPath().exists()) {
                initConfig()
                EncryptionAES(null)
            } else
                println("Config file was not initialized, because it already exists")
            if (!getDBPath().exists()) {
                getDBPath().createFile()
                DB().init()
            } else
                println("Config file was not initialized, because it already exists")
        } else if (forceInitialize) { // --forceinit
            initConfig()
            EncryptionAES(null)
            getDBPath().deleteIfExists()
            getDBPath().createFile()
            DB().init()
        } else if (this::fileUpload.isInitialized) { // --upload
            return upload()
        } else if (this::fileIdDownload.isInitialized) { // --download
            return download()
        }
        return 0
    }

    private fun upload(): Int {
        val files: MutableList<Path> = mutableListOf()
        if (fileUpload.isDirectory()) {
            files.addAll(listAllFiles(fileUpload))
        } else if (fileUpload.isRegularFile()) {
            files.add(fileUpload)
        } else
            return 1
        val db = DB()
        val encryptionNumber = 0
        val numberOfShards = 2


        for (file in files) {
            val generatedFileId = UUID.randomUUID().toString()

            val encryption: EncryptionAbstract
            when (getEncryptionAlgorithm(encryptionNumber)) {
                EncryptionType.AES -> encryption = EncryptionAES(getEncryptionKey(0, EncryptionType.AES))
            }

            //encrypt
            encryption.encryptFiles(
                listOf(file),
                listOf(file)
            )

            // split file into shards
            val fileShards = splitFile(file, numberOfShards, generatedFileId)

            // upload file shards
            val providers = getProviders(numberOfShards)
            for (i in providers.indices)
                providers[i].upload(fileShards[i])

            // on success: add file to db
            db.addFile(
                FileTypeDB(
                    generatedFileId,
                    file.name,
                    providers.map { it.id },
                    getEncryptionId(encryptionNumber)
                )
            )
        }
        return 0
    }

    private fun download(): Int {
        val db = DB()
        val encryptionNumber = 0
        // get file information
        val file = db.getFileByFileId(fileIdDownload)

        // download file shards
        val providers: MutableList<Provider> = mutableListOf()
        for (id in file.providerIdList) {
            providers.add(getProviderById(id))
        }

        val fileShards: MutableList<Path> = mutableListOf()
        for (i in 0..<providers.size) {
            fileShards.add(providers[i].download(file.fileId, i + 1))
        }

        // put file shards back together
        joinFiles(file.fileId, fileShards.size)
        val inputPaths = listOf(Path("${getTmpFolder()}/${file.fileId}"))
        val outputPaths = listOf(Path(file.name))

        val encryption: EncryptionAbstract
        when (getEncryptionAlgorithm(encryptionNumber)) {
            EncryptionType.AES -> encryption = EncryptionAES(getEncryptionKey(0, EncryptionType.AES))
        }

        //decrypt
        encryption.decryptFiles(
            inputPaths,
            outputPaths
        )

        return 0
    }
}

fun main(args: Array<String>): Unit = exitProcess(CommandLine(ScatterStore()).execute(*args))