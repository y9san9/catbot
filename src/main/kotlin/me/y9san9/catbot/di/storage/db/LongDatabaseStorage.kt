package me.y9san9.catbot.di.storage.db

import me.y9san9.catbot.di.requests.context.CatbotContext
import me.y9san9.catbot.di.requests.context.ChatMemberContext
import me.y9san9.catbot.di.storage.Storage
import me.y9san9.catbot.util.unit
import me.y9san9.db.migrations.MigrationsApplier
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class LongDatabaseStorage(
    url: String,
    user: String,
    password: String
) : DatabaseStorage.LongStorage {
    private val db = Database.connect(
        url = url,
        user = user,
        password = password
    )

    override suspend fun init(bot: CatbotContext.WithLongChatId) = newSuspendedTransaction(db = db) {
        when (bot) {
            is CatbotContext.Telegram -> {
                SchemaUtils.create(LongJoinHistoryTable)
                MigrationsApplier.apply(db, migrations)
            }
        }
    }

    override suspend fun saveUserJoined(user: ChatMemberContext.WithLongId) = newSuspendedTransaction(db = db) {
        when (user) {
            is ChatMemberContext.Telegram -> LongJoinHistoryTable.insert { statement ->
                statement[chatId] = user.chat.id
                statement[userId] = user.id
            }
        }
    }.unit

    override suspend fun isUserJoinedFirstTime(user: ChatMemberContext.WithLongId): Boolean =
        newSuspendedTransaction(db = db) {
            LongJoinHistoryTable.select {
                when (user) {
                    is ChatMemberContext.Telegram ->
                        (LongJoinHistoryTable.chatId eq user.chat.id) and (LongJoinHistoryTable.userId eq user.id)
                }
            }.firstOrNull() == null
        }
}
