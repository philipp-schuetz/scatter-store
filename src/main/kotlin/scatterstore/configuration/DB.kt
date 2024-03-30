package scatterstore.configuration

import scatterstore.getDBPath
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
        val fileId = varchar("file_id")
        val name = varchar("name")
        val providerIdList = varchar("provider_id_list")
        val encryptionId = varchar("encryption_id")
    }

    /**
     * Create an empty table in the database.
     */
    fun init() {
        database.useConnection { conn ->
            val sql = """
                create table t_file (
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
     * Checks if a file ID is already in use in the database.
     *
     * This function queries the database for the provided file ID. If the file ID is found in the database,
     * the function returns true. If the file ID is not found, the function returns false.
     *
     * @param id The file ID to check.
     * @return Boolean value indicating whether the file ID is in use.
     */
    fun fileIdInUse(id: String): Boolean {
        val response = database
            .from(FilesTable)
            .select()
            .where { (FilesTable.fileId eq id) }
            .limit(1)
            .totalRecordsInAllPages

        return response > 0
    }

    /**
     * Add a file to the database.
     * @param file File to add.
     */
    fun addFile(file: FileTypeDB) {
        database.insert(FilesTable) {
            set(FilesTable.fileId, file.fileId)
            set(FilesTable.name, file.name)
            set(FilesTable.providerIdList, file.providerIdList.joinToString(","))
            set(FilesTable.encryptionId, file.encryptionId)
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