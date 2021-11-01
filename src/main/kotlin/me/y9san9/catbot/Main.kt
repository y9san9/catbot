package me.y9san9.catbot

import dev.inmo.micro_utils.coroutines.safely
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.plus
import me.y9san9.catbot.di.startNewDefaultInstanceSafely

suspend fun main() = coroutineScope {
    val botToken = getEnvOrFail("BOT_TOKEN")
    val logChatId = getEnvOrNull("LOG_CHAT_ID")?.toLongOrNull()
    val databaseUrl = getEnvOrFail("DATABASE_URL")
    val databaseUser = getEnvOrFail("DATABASE_USER")
    val databasePassword = getEnvOrFail("DATABASE_PASSWORD")

    CatBot
        .startNewDefaultInstanceSafely(
            scope = this + CoroutineName("Catbot Coroutine"),
            token = botToken,
            logChatId = logChatId,
            databaseUrl = databaseUrl,
            databaseUser = databaseUser,
            databasePassword = databasePassword
        )
}
