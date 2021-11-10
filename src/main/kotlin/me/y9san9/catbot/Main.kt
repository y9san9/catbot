package me.y9san9.catbot

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.plus
import me.y9san9.catbot.di.CatGifsProviderType
import me.y9san9.catbot.di.startNewDefaultInstanceSafely

suspend fun main() = coroutineScope {
    val botToken = getEnvOrFail("BOT_TOKEN")
    val databaseUrl = getEnvOrFail("DATABASE_URL")
    val databaseUser = getEnvOrFail("DATABASE_USER")
    val databasePassword = getEnvOrFail("DATABASE_PASSWORD")

    val logChatId = getEnvOrNull("LOG_CHAT_ID")?.toLongOrNull()

    val catgifsProviderType = when (val input = getEnvOrNull("CATGIFS_PROVIDER_TYPE")?.lowercase()) {
        null, "catgifs" -> CatGifsProviderType.Cataas
        "local" -> CatGifsProviderType.Local
        else -> failWithReason("Invalid input for CATGIFS_PROVIDER_TYPE: $input")
    }

    CatBot
        .startNewDefaultInstanceSafely(
            scope = this + CoroutineName("Catbot Coroutine"),
            token = botToken,
            databaseUrl = databaseUrl,
            databaseUser = databaseUser,
            databasePassword = databasePassword,
            logChatId = logChatId,
            catGifsProviderType = catgifsProviderType
        )
}
