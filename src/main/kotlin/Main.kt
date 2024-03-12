package com.philippschuetz

import com.philippschuetz.configuration.ConfigModel
import com.philippschuetz.configuration.ConfigModelSectionEncryption
import com.philippschuetz.configuration.ConfigModelSectionProvider
import picocli.CommandLine
import java.util.concurrent.Callable
import kotlin.system.exitProcess


@CommandLine.Command(
    name = "scatter-store", mixinStandardHelpOptions = true, version = ["scatter-store 1.0"],
    description = ["Encrypts, splits and uploads files to different cloud storage providers."]
)
class ScatterStore : Callable<Int> {

    @CommandLine.Option(names = ["-f", "--file"], description = ["RegEx for files to upload."])
    lateinit var file: Regex

    @CommandLine.Option(names = ["-k", "--key"], description = ["Key for file encryption, see config. default: 0"])
    var algorithm: Int = 0

    @CommandLine.Option(names = ["-p", "--provider"], description = ["Add a cloud storage provider."])
    lateinit var provider: ProviderType


    override fun call(): Int {
        ConfigModel(
            listOf(ConfigModelSectionProvider("1111", "Test Provider", ProviderType.FTP, "ftp1", "123")),
            listOf(ConfigModelSectionEncryption("2222", EncryptionType.AES, "paoipaf398n308w80fn"))
        )
        return 0
    }
}

fun main(args: Array<String>): Unit = exitProcess(CommandLine(ScatterStore()).execute(*args))