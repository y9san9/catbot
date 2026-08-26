package me.y9san9.db.migrations

import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.deleteAll
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction


internal class MigrationsStorage (
    private val database: Database,
    tableName: String
) {
    private val table = object : Table(tableName) {
        val schemaVersion = integer(name = "schemaVersion")
    }

    init {
        transaction(database) {
            SchemaUtils.create(table)
        }
    }

    fun getVersion(): Int? = transaction(database) {
        table.selectAll().singleOrNull()?.get(table.schemaVersion)
    }

    fun setVersion(value: Int) {
        transaction(database) {
            table.deleteAll()
            table.insert {
                it[schemaVersion] = value
            }
        }
    }
}
