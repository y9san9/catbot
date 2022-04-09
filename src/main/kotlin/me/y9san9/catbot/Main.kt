package me.y9san9.catbot

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.plus
import me.y9san9.catbot.di.CatGifsProviderType
import me.y9san9.catbot.di.startNewDefaultInstanceSafely

suspend fun main() = coroutineScope {
    val botToken = getEnvOrFail("BOT_TOKEN")
    val logChatId = getEnvOrNull("LOG_CHAT_ID")?.toLongOrNull()
    val cachedGifsChatId = getEnvOrFail("CACHED_GIFS_CHAT_ID").toLongOrNull() ?: failWithReason("Invalid long number for CACHED_GIFS_CHAT_ID")
    val databaseUrl = getEnvOrFail("DATABASE_URL")
    val databaseUser = getEnvOrFail("DATABASE_USER")
    val databasePassword = getEnvOrFail("DATABASE_PASSWORD")

    val catgifsProviderType = when (val input = getEnvOrNull("CATGIFS_PROVIDER_TYPE")?.lowercase()) {
        null, "cataas" -> CatGifsProviderType.Cataas
        "local" -> CatGifsProviderType.Local
        else -> failWithReason("Invalid input for CATGIFS_PROVIDER_TYPE: $input")
    }

    TelegramCatBot
        .startNewDefaultInstanceSafely(
            scope = this + CoroutineName("Catbot Coroutine"),
            token = botToken,
            logChatId = logChatId,
            cachedGifsChatId = cachedGifsChatId,
            databaseUrl = databaseUrl,
            databaseUser = databaseUser,
            databasePassword = databasePassword,
            catGifsProviderType = catgifsProviderType
        )
}
