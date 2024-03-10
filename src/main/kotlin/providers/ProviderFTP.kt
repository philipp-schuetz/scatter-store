//package com.philippschuetz.providers
//
//class ProviderFTP(var username: String, var password: String, var remoteHost: String, var remoteDir: String): Provider {
//
//    val jsch: JSch = JSch()
//    jsch.setKnownHosts("/Users/john/.ssh/known_hosts")
//    val jschSession: Session = jsch.getSession(username, remoteHost)
//    jschSession.setPassword(password)
//    jschSession.connect()
//    return jschSession.openChannel("sftp") as ChannelSftp
//
//    override fun upload(fileId: String, fileNumber: Int) {
//        val channelSftp: ChannelSftp = setupJsch()
//        channelSftp.connect()
//
//        val fileName = "${fileId}_${fileNumber}"
//
//        channelSftp.put("/tmp/$fileName", "$remoteDir/$fileName")
//
//        channelSftp.exit()
//    }
//
//    override fun download(fileId: String) {
//        TODO("Not yet implemented")
//    }
//
//    override fun delete(fileId: String) {
//        TODO("Not yet implemented")
//    }
//}