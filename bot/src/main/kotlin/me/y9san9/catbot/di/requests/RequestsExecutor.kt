package me.y9san9.catbot.di.requests

import java.io.File

interface RequestsExecutor {
    suspend fun sendGif(chatId: Long, gif: File)
}
