package com.philippschuetz.configuration

import com.philippschuetz.EncryptionType
import com.philippschuetz.ProviderType
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class ConfigSectionProvider(
    val id: String,
    val name: String,
    val type: ProviderType,
    val username: String,
    val password: String,
)

@Serializable
data class ConfigSectionEncryption(val id: String, val algorithm: EncryptionType, val key: String)

@Serializable
data class Config(val providers: List<ConfigSectionProvider>, val encryption: List<ConfigSectionEncryption>)

fun main() {
    val data = Config(
        listOf(ConfigSectionProvider("1111", "Test Provider", ProviderType.FTP, "ftp1", "123")),
        listOf(ConfigSectionEncryption("2222", EncryptionType.AES, "paoipaf398n308w80fn"))
    )
    println(data)

    val json = Json.encodeToString(data)
    println(json)

    val obj = Json.decodeFromString<Config>(json)
    println(json)
}