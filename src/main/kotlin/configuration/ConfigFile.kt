package com.philippschuetz.configuration

import com.philippschuetz.EncryptionType
import com.philippschuetz.ProviderType
import java.net.URL

data class ConfigSectionFile(val id: String, val providers: List<ProviderType>, val encryption: String, val name: String)

data class ConfigSectionProvider(val name: ProviderType, val username: String, val password: String)
data class ConfigSectionProviderFTP(val name: String = "FTP", val username: String, val password: String, val url: URL, val port: Int)

data class ConfigSectionEncryption(val id: String, val algorithm: EncryptionType, val key: String)
data class Config(
    val files: List<ConfigSectionFile>,
    val providers: List<ConfigSectionProviderFTP>,
    val encryption: List<ConfigSectionEncryption>
)