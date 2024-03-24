package com.philippschuetz.providers

import java.nio.file.Path

interface Provider {
    val id: String

    /**
     * Uploads a file to the provider.
     *
     * @param file The path of the file to be uploaded.
     */
    fun upload(file: Path)

    /**
     * Downloads a file from the provider to the tmp directory.
     *
     * @param fileId The unique identifier of the file to be downloaded.
     * @param fileNumber The number of the file to be downloaded.
     * @return The path where the downloaded file is stored.
     */
    fun download(fileId: String, fileNumber: Int): Path
}