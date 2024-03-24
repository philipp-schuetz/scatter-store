package com.philippschuetz.providers

import com.philippschuetz.getTmpFolder
import net.schmizz.sshj.SSHClient
import net.schmizz.sshj.transport.verification.PromiscuousVerifier
import net.schmizz.sshj.xfer.FileSystemFile
import java.nio.file.Path
import kotlin.io.path.Path

/**
 * This class represents an FTP provider.
 * It uses SSH for secure file transfer.
 *
 * @param username The username for the FTP server.
 * @param password The password for the FTP server. Leave empty to use key-based auth.
 * @param keyAuth A boolean indicating whether to use key-based authentication.
 * @param remoteHost The host address of the FTP server.
 * @param port The port number of the FTP server.
 * @param remoteDir The directory on the FTP server to use.
 */
class ProviderFTP(
    override val id: String,
    username: String,
    password: String?,
    keyAuth: Boolean,
    remoteHost: String,
    port: Int,
    private var remoteDir: String
) :
    Provider {

    private val sshClient = SSHClient()

    init {
        sshClient.addHostKeyVerifier(PromiscuousVerifier())
        sshClient.connect(remoteHost, port)
        sshClient.useCompression()
        if (!keyAuth)
            sshClient.authPassword(username, password)
        else
            sshClient.authPublickey(username)
    }

    override fun upload(file: Path) {
        val sftpClient = sshClient.newSFTPClient()

        sftpClient.put(FileSystemFile(file.toFile()), "$remoteDir/${file.fileName}")

        sftpClient.close()
        sshClient.disconnect()
    }

    override fun download(fileId: String, fileNumber: Int): Path {
        val sftpClient = sshClient.newSFTPClient()

        val localDownloadPath = Path("${getTmpFolder()}")
        sftpClient.get("$remoteDir/$fileId-$fileNumber", FileSystemFile(localDownloadPath.toFile()))

        sftpClient.close()
        sshClient.disconnect()
        return localDownloadPath
    }
}