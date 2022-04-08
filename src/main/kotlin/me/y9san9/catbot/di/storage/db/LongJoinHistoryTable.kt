package me.y9san9.catbot.di.storage.db

import org.jetbrains.exposed.sql.Table

object LongJoinHistoryTable : Table() {
    val chatId = long("chatId")
    val userId = long("userId")
}
