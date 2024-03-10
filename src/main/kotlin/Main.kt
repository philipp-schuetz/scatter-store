package com.philippschuetz

import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.addResourceSource
import picocli.CommandLine
import java.util.concurrent.Callable
import kotlin.system.exitProcess


@CommandLine.Command(name = "scatter-store", mixinStandardHelpOptions = true, version = ["scatter-store 1.0"],
    description = ["Encrypts, splits and uploads files to different cloud storage providers."])
class ScatterStore : Callable<Int> {

    @CommandLine.Option(names = ["-f", "--file"], description = ["RegEx for files to upload."])
    lateinit var file: Regex

    @CommandLine.Option(names = ["-k", "--key"], description = ["Key for file encryption, see config. default: 0"])
    var algorithm: Int = 0

    @CommandLine.Option(names = ["-p", "--provider"], description = ["Add a cloud storage provider."])
    lateinit var provider: ProviderType


    override fun call(): Int {
        val config = ConfigLoaderBuilder.default()
            .addResourceSource("/scatter-store.json")
            .build()
            .loadConfigOrThrow<Config>()
        println(config)
        return 0
    }
}

fun main(args: Array<String>) : Unit = exitProcess(CommandLine(ScatterStore()).execute(*args))