package com.philippschuetz.providers

import net.schmizz.sshj.SSHClient

class ProviderFTP(
    private var username: String,
    private var password: String,
    private var remoteHost: String,
    private var port: Int,
    var remoteDir: String
) :
    Provider {

    init {
        val sshClient = SSHClient()
        sshClient.connect(remoteHost, port)
        sshClient.authPassword(username, password)
    }

    override fun upload(fileId: String, fileNumber: Int) {
    }

    override fun download(fileId: String) {
        TODO("Not yet implemented")
    }

    override fun delete(fileId: String) {
        TODO("Not yet implemented")
    }
}