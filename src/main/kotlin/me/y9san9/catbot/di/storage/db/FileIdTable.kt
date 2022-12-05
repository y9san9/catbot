package me.y9san9.catbot.di.storage.db

import org.jetbrains.exposed.sql.Table

object FileIdTable : Table() {
    val fileId = varchar("fileId", length = 128)
    val md5checksum = varchar("md5checksum", length = 32)
}
