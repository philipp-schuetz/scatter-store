package scatterstore.providers

import com.jcraft.jsch.ChannelSftp
import com.jcraft.jsch.JSch
import com.jcraft.jsch.Session
import scatterstore.getTmpFolder
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
    private val username: String,
    private val password: String?,
    private val keyAuth: Boolean,
    private val remoteHost: String,
    private val port: Int,
    private val remoteDir: String
) : Provider {

    private fun createSession(): Session{
        val jsch = JSch()
        val session: Session
        println(1)
        if (!keyAuth) {
            session = jsch.getSession(username, remoteHost, port)
            session.setPassword(password)
        } else {
            jsch.addIdentity(username)
            session = jsch.getSession(username, remoteHost, port)
        }
        session.setConfig("StrictHostKeyChecking", "no")
        session.connect()
        return session
    }

    override fun upload(file: Path) {
        val session = createSession()
        val channel = session.openChannel("sftp") as ChannelSftp
        channel.connect()
        channel.put(file.toString(), "$remoteDir/${file.fileName}")
        channel.disconnect()
        session.disconnect()
    }

    override fun download(fileId: String, fileNumber: Int): Path {
        val session = createSession()
        val channel = session.openChannel("sftp") as ChannelSftp
        channel.connect()

        val localDownloadPath = Path("${getTmpFolder()}/$fileId-$fileNumber")
        channel.get("$remoteDir/$fileId-$fileNumber", localDownloadPath.toString())

        channel.disconnect()
        session.disconnect()
        return localDownloadPath
    }
}