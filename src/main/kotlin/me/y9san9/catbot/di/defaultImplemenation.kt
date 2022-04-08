package me.y9san9.catbot.di

import dev.inmo.tgbotapi.bot.Ktor.telegramBot
import kotlinx.coroutines.CoroutineScope
import me.y9san9.catbot.CatBotDependencies
import me.y9san9.catbot.CatBot
import me.y9san9.catbot.di.catgifs.FromCacheCatgifsProvider
import me.y9san9.catbot.di.catgifs.KtorCatgifsProvider
import me.y9san9.catbot.di.log.BotLogger
import me.y9san9.catbot.di.log.PrintLogger
import me.y9san9.catbot.di.log.TelegramLogger
import me.y9san9.catbot.di.requests.context.TelegramContext
import me.y9san9.catbot.di.resources.DefaultStringsProvider
import me.y9san9.catbot.di.storage.MemoryFileIdStorage
import me.y9san9.catbot.di.storage.db.DatabaseStorage
import me.y9san9.catbot.di.storage.db.LongDatabaseStorage

enum class CatGifsProviderType {
    Local, Cataas
}

fun CatBot.startNewDefaultInstanceSafely(
    scope: CoroutineScope,
    token: String,
    logChatId: Long?,
    cachedGifsChatId: Long,
    databaseUrl: String,
    databaseUser: String,
    databasePassword: String,
    catGifsProviderType: CatGifsProviderType
) {
    val telegramBot = telegramBot(token)
    val logWriter = when (logChatId) {
        null -> PrintLogger
        else -> TelegramLogger(logChatId, telegramBot, scope)
    }

    startNewInstanceSafely(
        scope = scope,
        botStarter = { safeScope ->
            val catgifsProvider = when (catGifsProviderType) {
                CatGifsProviderType.Cataas -> KtorCatgifsProvider(safeScope) { _, message ->
                    logWriter.log(message)
                }
                CatGifsProviderType.Local -> FromCacheCatgifsProvider(safeScope) { _, message ->
                    logWriter.log(message)
                }
            }

            val dependencies = CatBotDependencies(
                executor = TelegramContext(scope, telegramBot, MemoryFileIdStorage, cachedGifsChatId),
                catGifs = catgifsProvider,
                stringsProvider = DefaultStringsProvider,
                storage = DatabaseStorage(
                    LongDatabaseStorage(
                        url = databaseUrl,
                        user = databaseUser,
                        password = databasePassword
                    )
                ),
                logger = BotLogger { _, message -> logWriter.log(message) },
                scope = safeScope
            )

            startNewInstance(dependencies)
        },
        logWriter = logWriter
    )
}
