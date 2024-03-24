package com.philippschuetz.configuration

import com.philippschuetz.EncryptionType
import com.philippschuetz.getConfigPath
import com.philippschuetz.getRandomString
import com.philippschuetz.providers.Provider
import com.philippschuetz.providers.ProviderFTP
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*
import kotlin.io.path.readText
import kotlin.io.path.writeText
import kotlin.system.exitProcess

private object ConfigModelSectionProviderSerializer :
    JsonContentPolymorphicSerializer<ConfigModelSectionProvider>(ConfigModelSectionProvider::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<ConfigModelSectionProvider> {
        val jsonObject = element as? JsonObject
        return when {
            jsonObject?.containsKey("username") == true &&
                    jsonObject.containsKey("password") &&
                    jsonObject.containsKey("keyAuth") &&
                    jsonObject.containsKey("remoteHost") &&
                    jsonObject.containsKey("port") &&
                    jsonObject.containsKey("remoteDir") -> ConfigModelSectionProviderFTP.serializer()

            else -> error("Cannot determine type of ConfigModelSectionProvider")
        }
    }
}

@Serializable(with = ConfigModelSectionProviderSerializer::class)
private interface ConfigModelSectionProvider {
    var id: String
    var name: String
}

@Serializable
private data class ConfigModelSectionProviderFTP(
    override var id: String,
    override var name: String,
    var username: String,
    var password: String,
    var keyAuth: Boolean,
    var remoteHost: String,
    var port: Int,
    var remoteDir: String
) : ConfigModelSectionProvider

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

fun getEncryptionId(keyIndex: Int): String {
    return readConfig().encryption[keyIndex].id
}

fun getEncryptionAlgorithm(keyIndex: Int): EncryptionType {
    return readConfig().encryption[keyIndex].algorithm
}

/**
 * Get the first x Providers in the config file.
 * @param quantity The Number of Providers to get.
 */
fun getProviders(quantity: Int): List<Provider> {
    val out: MutableList<Provider> = mutableListOf()
    val providers = readConfig().providers
    for (i in 0..quantity) {
        when (val provider = providers[i]) {
            is ConfigModelSectionProviderFTP -> out.add(
                ProviderFTP(
                    provider.id,
                    provider.username,
                    provider.password,
                    provider.keyAuth,
                    provider.remoteHost,
                    provider.port,
                    provider.remoteDir
                )
            )
        }
    }
    return out
}

/**
 * Write an empty config file.
 */
fun initConfig() {
    writeConfig(ConfigModel(listOf(), listOf()))
}