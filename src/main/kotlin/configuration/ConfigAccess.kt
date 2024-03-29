package com.philippschuetz.configuration

import com.philippschuetz.EncryptionType
import com.philippschuetz.ProviderType
import com.philippschuetz.getConfigPath
import com.philippschuetz.getRandomString
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.io.path.readText
import kotlin.io.path.writeText
import kotlin.system.exitProcess

@Serializable
private data class ConfigModelSectionProvider(
    var id: String,
    var name: String,
    var type: ProviderType,
    var username: String,
    var password: String,
)

@Serializable
private data class ConfigModelSectionEncryption(var id: String, var algorithm: EncryptionType, var key: String)

@Serializable
private data class ConfigModel(
    var providers: List<ConfigModelSectionProvider>,
    var encryption: List<ConfigModelSectionEncryption>
)

/**
 * Load the config and return the serialized version.
 * @param reload If false, only the cached version of the config is returned. If true, the config is reloaded from the file before returning.
 */
private fun readConfig(reload: Boolean = false): ConfigModel {
    if (reload || config == null)
        config = Json.decodeFromString<ConfigModel>(getConfigPath().readText())
    return config as ConfigModel
}

/**
 * Encode content, change the cached config and write it to file.
 * @param newContent New config content to use.
 */
private fun writeConfig(newContent: ConfigModel) {
    config = newContent
    getConfigPath().writeText(Json.encodeToString(newContent))
}

private var config: ConfigModel? = null

private fun <T> idInList(list: List<T>, id: String, idExtractor: (T) -> String): Boolean {
    return list.any { idExtractor(it) == id }
}

fun addEncryptionKey(algorithm: EncryptionType, key: String) {
    val modifiedConfig = readConfig()
    var generatedId: String
    modifiedConfig.encryption[0].id
    do {
        generatedId = getRandomString(8)
    } while (idInList(modifiedConfig.encryption, generatedId) { it.id })

    modifiedConfig.encryption += ConfigModelSectionEncryption(generatedId, algorithm, key)
    writeConfig(modifiedConfig)
}

fun getEncryptionKey(keyIndex: Int, algorithm: EncryptionType): String {
    val modifiedConfig = readConfig().encryption[keyIndex]
    if (modifiedConfig.algorithm != algorithm) {
        println("given key index does not match used algorithm")
        exitProcess(1)
    }
    return modifiedConfig.key
}