package com.philippschuetz.configuration

import com.philippschuetz.getDataFolder
import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.ktorm.schema.*

data class FileTypeDB(val fileId: String, val name: String, val providerIdList: List<String>, val encryptionId: String)

class DB {
    private val database: Database = Database.connect("jdbc:sqlite:${getDataFolder()}/scatter-store.sqlite")

    object FilesTable : Table<Nothing>("t_file") {
        val id = int("id").primaryKey()
        val fileId = varchar("file_id")
        val name = varchar("name")
        val providerIdList = varchar("provider_id_list")
        val encryptionId = varchar("encryption_id")
    }

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

    fun addFile(file: FileTypeDB) {
        database.insert(FilesTable) {
            set(it.fileId, file.fileId)
            set(it.name, file.name)
            set(it.providerIdList, file.providerIdList.joinToString(","))
            set(it.encryptionId, file.encryptionId)
        }
    }

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

    fun getAllFiles() {
        for (row in database.from(FilesTable).select()) {
            println(row)
        }
    }

}


fun main() {
    DB().init()
}