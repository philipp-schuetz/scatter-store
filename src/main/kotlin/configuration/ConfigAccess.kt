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
 * @return The loaded configuration model.
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

/**
 * Check if a given id exists in a list.
 * @param list The list to check.
 * @param id The id to look for.
 * @param idExtractor A function to extract the id from an item in the list.
 * @return True if the id exists in the list, false otherwise.
 */
private fun <T> idInList(list: List<T>, id: String, idExtractor: (T) -> String): Boolean {
    return list.any { idExtractor(it) == id }
}

/**
 * Add a new encryption key to the configuration file.
 * @param algorithm The encryption algorithm associated with the key.
 * @param key The encryption key.
 */
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

/**
 * Retrieve an encryption key from the configuration file.
 * @param keyIndex The index of the key in the configuration file.
 * @param algorithm The encryption algorithm associated with the key.
 * @return The encryption key.
 */
fun getEncryptionKey(keyIndex: Int, algorithm: EncryptionType): String {
    val modifiedConfig = readConfig().encryption[keyIndex]
    if (modifiedConfig.algorithm != algorithm) {
        println("given key index does not match used algorithm")
        exitProcess(1)
    }
    return modifiedConfig.key
}

/**
 * Write an empty config file.
 */
fun initConfig() {
    writeConfig(ConfigModel(listOf(), listOf()))
}