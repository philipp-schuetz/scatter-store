package com.philippschuetz.providers

interface Provider {
    fun upload(fileId: String, fileNumber: Int)
    fun download(fileId: String)
    fun delete(fileId: String)
}