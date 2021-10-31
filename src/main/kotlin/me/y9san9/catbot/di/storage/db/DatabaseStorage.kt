package me.y9san9.catbot.di.storage.db

import me.y9san9.catbot.di.requests.models.ChatMember
import me.y9san9.catbot.di.storage.Storage
import me.y9san9.catbot.util.unit
import me.y9san9.db.migrations.MigrationsApplier
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class DatabaseStorage(
    url: String,
    user: String,
    password: String
) : Storage {
    private val db = Database.connect(
        url = url,
        user = user,
        password = password
    )

    override suspend fun init() = newSuspendedTransaction(db = db) {
        SchemaUtils.create(JoinHistoryTable)
        MigrationsApplier.apply(db, migrations)
    }

    override suspend fun saveUserJoined(user: ChatMember) = newSuspendedTransaction(db = db) {
        JoinHistoryTable.insert { statement ->
            statement[chatId] = user.chat.id
            statement[userId] = user.id
        }
    }.unit

    override suspend fun isUserJoinedFirstTime(user: ChatMember): Boolean = newSuspendedTransaction(db = db) {
        JoinHistoryTable.select {
            (JoinHistoryTable.chatId eq user.chat.id) and (JoinHistoryTable.userId eq user.id)
        }.firstOrNull() == null
    }
}
