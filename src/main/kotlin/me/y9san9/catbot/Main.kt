package me.y9san9.catbot

import kotlinx.coroutines.coroutineScope
import me.y9san9.catbot.di.defaultImplementation

suspend fun main() = coroutineScope {
    val botToken = getEnvOrFail("BOT_TOKEN")
    val logChatId = getEnvOrNull("LOG_CHAT_ID")?.toLongOrNull()
    val databaseUrl = getEnvOrFail("DATABASE_URL")
    val databaseUser = getEnvOrFail("DATABASE_USER")
    val databasePassword = getEnvOrFail("DATABASE_PASSWORD")

    CatBot
        .defaultImplementation(
            scope = this,
            token = botToken,
            logChatId = logChatId,
            databaseUrl = databaseUrl,
            databaseUser = databaseUser,
            databasePassword = databasePassword
        )
        .startNewInstance()
}
