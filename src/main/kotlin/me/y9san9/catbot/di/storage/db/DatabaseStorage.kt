package me.y9san9.catbot.di.storage.db

import dev.inmo.tgbotapi.requests.abstracts.FileId
import me.y9san9.catbot.Storage
import me.y9san9.catbot.utils.getMD5
import me.y9san9.db.migrations.MigrationsApplier
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.update
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction
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
        suspendTransaction(db = db) {
            SchemaUtils.create(LongJoinHistoryTable, ChatSettingsTable, FileIdTable)
        }
        MigrationsApplier.apply(db, migrations)
    }

    override suspend fun isUserJoinedFirstTime(chatId: Long, userId: Long): Boolean =
        suspendTransaction(db = db) {
            LongJoinHistoryTable
                .selectAll()
                .where { (LongJoinHistoryTable.chatId eq chatId) and (LongJoinHistoryTable.userId eq userId) }
                .firstOrNull() == null
        }

    override suspend fun saveUserJoined(chatId: Long, userId: Long) {
        suspendTransaction(db = db) {
            LongJoinHistoryTable.insert { statement ->
                statement[LongJoinHistoryTable.chatId] = chatId
                statement[LongJoinHistoryTable.userId] = userId
            }
        }
    }

    override suspend fun saveLanguageCodeForChat(chatId: Long, languageCode: String) {
        require(languageCode.length == 2)
        insertChatSettingsIfNeed(chatId)
        suspendTransaction(db = db) {
            ChatSettingsTable.update(where = { ChatSettingsTable.chatId eq chatId }) { statement ->
                statement[ChatSettingsTable.languageCode] = languageCode
            }
        }
    }

    override suspend fun getLanguageCodeForChat(chatId: Long): String? =
        suspendTransaction(db = db) {
            ChatSettingsTable.selectAll()
                .where { ChatSettingsTable.chatId eq chatId }
                .singleOrNull()?.get(ChatSettingsTable.languageCode)
        }

    private suspend fun insertChatSettingsIfNeed(chatId: Long) {
        suspendTransaction(db = db) {
            if (ChatSettingsTable.selectAll().where { ChatSettingsTable.chatId eq chatId }.firstOrNull() == null)
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
        suspendTransaction(db = db) {
            FileIdTable.insert { statement ->
                statement[FileIdTable.fileId] = fileId.fileId
                statement[FileIdTable.md5checksum] = checksum
            }
        }
    }

    private suspend fun getFileId(checksum: String): FileId? =
        suspendTransaction(db = db) {
            FileIdTable.selectAll()
                .where { FileIdTable.md5checksum eq checksum }
                .firstOrNull()?.get(FileIdTable.fileId)?.let(::FileId)
        }

}
