package me.y9san9.catbot.di.storage.db

import org.jetbrains.exposed.sql.Table

object ChatSettingsTable : Table() {
    val chatId = long("chatId")
    val languageCode = varchar("languageCode", length = 2).nullable()
}
