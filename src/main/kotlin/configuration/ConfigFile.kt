package com.philippschuetz.configuration

import com.philippschuetz.EncryptionType
import com.philippschuetz.ProviderType
import com.philippschuetz.getConfigPath
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.io.path.readText
import kotlin.io.path.writeText

@Serializable
data class ConfigModelSectionProvider(
    var id: String,
    var name: String,
    var type: ProviderType,
    var username: String,
    var password: String,
)

@Serializable
data class ConfigModelSectionEncryption(var id: String, var algorithm: EncryptionType, var key: String)

@Serializable
data class ConfigModel(
    var providers: List<ConfigModelSectionProvider>,
    var encryption: List<ConfigModelSectionEncryption>
)

/**
 * Load the config and return the serialized version.
 * @param reload If false, only the cached version of the config is returned. If true, the config is reloaded from the file before returning.
 */
fun readConfig(reload: Boolean = false): ConfigModel {
    if (reload || config == null)
        config = Json.decodeFromString<ConfigModel>(getConfigPath().readText())
    return config as ConfigModel
}

/**
 * Encode content, change the cached config and write it to file.
 * @param newContent New config content to use.
 */
fun writeConfig(newContent: ConfigModel) {
    config = newContent
    getConfigPath().writeText(Json.encodeToString(newContent))
}

private var config: ConfigModel? = null