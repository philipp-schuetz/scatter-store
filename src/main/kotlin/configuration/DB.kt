package com.philippschuetz.configuration

import com.philippschuetz.getDBPath
import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.ktorm.logging.ConsoleLogger
import org.ktorm.logging.LogLevel
import org.ktorm.schema.*

data class FileTypeDB(val fileId: String, val name: String, val providerIdList: List<String>, val encryptionId: String)

class DB {
    private val database: Database = Database.connect(
        url = "jdbc:sqlite:${getDBPath()}",
        logger = ConsoleLogger(LogLevel.ERROR)
    )

    object FilesTable : Table<Nothing>("t_file") {
        val id = int("id").primaryKey()
        val fileId = varchar("file_id")
        val name = varchar("name")
        val providerIdList = varchar("provider_id_list")
        val encryptionId = varchar("encryption_id")
    }

    /**
     * Create an empty file table in the database.
     */
    fun init() {
        database.useConnection { conn ->
            val sql = """
                create table t_file (
                    id int,
                    file_id varchar(8),
                    name varchar(255),
                    provider_id_list varchar(255),
                    encryption_id int
                )
            """

            conn.prepareStatement(sql).use { statement ->
                statement.executeUpdate()
            }
        }
    }

    /**
     * Add a file to the database.
     * @param file File to add.
     */
    fun addFile(file: FileTypeDB) {
        database.insert(FilesTable) {
            set(it.fileId, file.fileId)
            set(it.name, file.name)
            set(it.providerIdList, file.providerIdList.joinToString(","))
            set(it.encryptionId, file.encryptionId)
        }
    }

    /**
     * Get information on a file using its unique identifier.
     * @param fileId Unique file identifier.
     */
    fun getFileByFileId(fileId: String): FileTypeDB {
        val response = database
            .from(FilesTable)
            .select()
            .where { (FilesTable.fileId eq fileId) }
            .map { row ->
                FileTypeDB(
                    fileId = row[FilesTable.fileId]!!,
                    name = row[FilesTable.name]!!,
                    providerIdList = row[FilesTable.providerIdList]!!.split(","),
                    encryptionId = row[FilesTable.encryptionId]!!
                )
            }

        return response.first()
    }

    /**
     * Get information on a file by searching for its original name.
     * @param fileName Original file name.
     */
    fun getFileByFileName(fileName: String): List<FileTypeDB> {
        val response = database
            .from(FilesTable)
            .select()
            .where { (FilesTable.name like "%${fileName.lowercase()}%") }
            .map { row ->
                FileTypeDB(
                    fileId = row[FilesTable.fileId]!!,
                    name = row[FilesTable.name]!!,
                    providerIdList = row[FilesTable.providerIdList]!!.split(","),
                    encryptionId = row[FilesTable.encryptionId]!!
                )
            }

        return response
    }
}