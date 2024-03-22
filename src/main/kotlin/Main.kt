package com.philippschuetz

import com.philippschuetz.configuration.*
import com.philippschuetz.encryption.EncryptionAES
import com.philippschuetz.splitting.splitFile
import picocli.CommandLine
import java.nio.file.Path
import java.util.concurrent.Callable
import kotlin.io.path.*
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
            println("log1")
            println("file exists? ${file.exists()}")
            val files: MutableList<Path> = mutableListOf()
            if (file.isDirectory()) {
                files.addAll(listAllFiles(file))
            } else if (file.isRegularFile()) {
                files.add(file)
            } else
                return 1
            val db = DB()
            val encryptionNumber = 0
            val numberOfShards = 2


            for (file in files) {
                var generatedFileId: String
                do {
                    generatedFileId = getRandomString(8)
                } while (db.fileIdInUse(generatedFileId))
                //encrypt
                when (getEncryptionAlgorithm(encryptionNumber)) {
                    EncryptionType.AES -> EncryptionAES().encryptFiles(listOf(file), encryptionNumber)
                }

                // split file into shards
                val fileShards = splitFile(file, numberOfShards, generatedFileId)

                // upload file shards
                val providers = getProviders(numberOfShards)
                for (i in 0..providers.size)
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
        }
        return 0
    }
}

fun main(args: Array<String>): Unit = exitProcess(CommandLine(ScatterStore()).execute(*args))