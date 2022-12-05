package me.y9san9.catbot

import dev.inmo.tgbotapi.requests.abstracts.FileId
import java.io.File

interface Storage {
    suspend fun init()
    suspend fun isUserJoinedFirstTime(chatId: Long, userId: Long): Boolean
    suspend fun saveUserJoined(chatId: Long, userId: Long)
    suspend fun saveLanguageCodeForChat(chatId: Long, languageCode: String)
    suspend fun getLanguageCodeForChat(chatId: Long): String?
    suspend fun getFileId(file: File): FileId?
    suspend fun putFileId(file: File, fileId: FileId)
}
