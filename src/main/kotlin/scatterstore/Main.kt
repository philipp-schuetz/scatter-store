package scatterstore

import scatterstore.encryption.EncryptionAES
import scatterstore.splitting.splitFile
import picocli.CommandLine
import scatterstore.configuration.*
import scatterstore.providers.Provider
import scatterstore.splitting.joinFiles
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

    @CommandLine.Option(names = ["--upload"], description = ["Upload a file. (Directory or File)"])
    private lateinit var fileUpload: Path

    @CommandLine.Option(names = ["--download"], description = ["Download a file. (File ID)"])
    private lateinit var fileIdDownload: String

    override fun call(): Int {
        if (initialize) { // --init
            // create an empty config file with an AES Key, create a database file and add db structure
            initConfig()
            EncryptionAES().generateKey()
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
        // get file information
        val file = db.getFileByFileId(fileIdDownload)

        // download file shards
        val providers: MutableList<Provider> = mutableListOf()
        for (id in file.providerIdList) {
            providers.add(getProviderById(id))
        }

        val fileShards: MutableList<Path> = mutableListOf()
        for (i in 0..<providers.size) {
            fileShards.add(providers[i].download(file.fileId, i+1))
        }

        // put file shards back together
        joinFiles(file.fileId, fileShards.size, Path(file.name))

        return 0
    }
}

fun main(args: Array<String>): Unit = exitProcess(CommandLine(ScatterStore()).execute(*args))