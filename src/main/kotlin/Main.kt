package com.philippschuetz

import com.philippschuetz.configuration.DB
import com.philippschuetz.configuration.initConfig
import com.philippschuetz.encryption.EncryptionAES
import picocli.CommandLine
import java.nio.file.Path
import java.util.concurrent.Callable
import kotlin.io.path.createFile
import kotlin.io.path.isDirectory
import kotlin.io.path.isRegularFile
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
        if (initialize) {
            // create an empty config file with an AES Key, create a database file and add db structure
            initConfig()
            EncryptionAES().generateKey()
            getDBPath().createFile()
            DB().init()
        } else if (this::file.isInitialized) {
            val files: MutableList<Path> = mutableListOf()
            if (file.isDirectory()) {
                TODO("scan directory for files recursively")
            } else if (file.isRegularFile()) {
                files.add(file)
            } else
                return 1
        }
        return 0
    }
}

fun main(args: Array<String>): Unit = exitProcess(CommandLine(ScatterStore()).execute(*args))