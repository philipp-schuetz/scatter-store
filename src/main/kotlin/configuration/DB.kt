package com.philippschuetz.configuration

import com.philippschuetz.getDataFolder
import org.ktorm.database.Database
import org.ktorm.dsl.from
import org.ktorm.dsl.select
import org.ktorm.schema.*

object Files : Table<Nothing>("t_file") {
    val id = int("id").primaryKey()
    val name = varchar("name")
    val providerIds = listOf(int("provider_id_list"))
    val encryptionId = int("encryption_id")
}

fun main() {
    val database = Database.connect("jdbc:sqlite:${getDataFolder()}/scatter-store.sqlite")

    for (row in database.from(Files).select()) {
        println(row)
    }
}