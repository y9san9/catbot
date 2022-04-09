package me.y9san9.catbot.di.storage.db

import dev.inmo.tgbotapi.requests.abstracts.FileId
import me.y9san9.catbot.Storage
import me.y9san9.catbot.utils.getMD5
import me.y9san9.db.migrations.MigrationsApplier
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.io.File

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

    override suspend fun init() {
        newSuspendedTransaction(db = db) {
            SchemaUtils.create(LongJoinHistoryTable, ChatSettingsTable, FileIdTable)
        }
        MigrationsApplier.apply(db, migrations)
    }

    override suspend fun isUserJoinedFirstTime(chatId: Long, userId: Long): Boolean =
        newSuspendedTransaction(db = db) {
            LongJoinHistoryTable.select {
                (LongJoinHistoryTable.chatId eq chatId) and (LongJoinHistoryTable.userId eq userId)
            }.firstOrNull() == null
        }

    override suspend fun saveUserJoined(chatId: Long, userId: Long) {
        newSuspendedTransaction(db = db) {
            LongJoinHistoryTable.insert { statement ->
                statement[LongJoinHistoryTable.chatId] = userId
                statement[LongJoinHistoryTable.userId] = chatId
            }
        }
    }

    override suspend fun saveLanguageCodeForChat(chatId: Long, languageCode: String) {
        require(languageCode.length == 2)
        insertChatSettingsIfNeed(chatId)
        newSuspendedTransaction(db = db) {
            ChatSettingsTable.update(where = { ChatSettingsTable.chatId eq chatId }) { statement ->
                statement[ChatSettingsTable.languageCode] = languageCode
            }
        }
    }

    override suspend fun getLanguageCodeForChat(chatId: Long): String? =
        newSuspendedTransaction(db = db) {
            ChatSettingsTable.select { ChatSettingsTable.chatId eq chatId }
                .singleOrNull()?.get(ChatSettingsTable.languageCode)
        }

    private suspend fun insertChatSettingsIfNeed(chatId: Long) {
        newSuspendedTransaction(db = db) {
            if (ChatSettingsTable.select { ChatSettingsTable.chatId eq chatId }.firstOrNull() == null)
                ChatSettingsTable.insert {
                    it[ChatSettingsTable.chatId] = chatId
                }
        }
    }

    override suspend fun getFileId(file: File): FileId? {
        val checksum = file.getMD5()
        return getFileId(checksum)
    }

    override suspend fun putFileId(file: File, fileId: FileId) {
        val checksum = file.getMD5()
        if (getFileId(checksum) != null) return
        newSuspendedTransaction(db = db) {
            FileIdTable.insert { statement ->
                statement[FileIdTable.fileId] = fileId.fileId
                statement[FileIdTable.md5checksum] = checksum
            }
        }
    }

    private suspend fun getFileId(checksum: String): FileId? =
        newSuspendedTransaction(db = db) {
            FileIdTable.select { FileIdTable.md5checksum eq checksum }.firstOrNull()
                ?.get(FileIdTable.fileId)?.let(::FileId)
        }

}
