package me.y9san9.catbot.di

import dev.inmo.tgbotapi.bot.Ktor.telegramBot
import dev.inmo.tgbotapi.types.ChatId
import kotlinx.coroutines.CoroutineScope
import me.y9san9.catbot.TelegramCatBot
import me.y9san9.catbot.TelegramCatBotDeps
import me.y9san9.catbot.di.catgifs.FromCacheCatgifsProvider
import me.y9san9.catbot.di.catgifs.KtorCatgifsProvider
import me.y9san9.catbot.di.log.BotLogger
import me.y9san9.catbot.di.log.PrintLogger
import me.y9san9.catbot.di.log.TelegramLogger
import me.y9san9.catbot.di.resources.DefaultStringsProvider
import me.y9san9.catbot.di.resources.createNyanCatFile
import me.y9san9.catbot.di.storage.db.DatabaseStorage

enum class CatGifsProviderType {
    Local, Cataas
}

fun TelegramCatBot.startNewDefaultInstanceSafely(
    scope: CoroutineScope,
    token: String,
    logChatId: Long?,
    cachedGifsChatId: Long,
    databaseUrl: String,
    databaseUser: String,
    databasePassword: String,
    catGifsProviderType: CatGifsProviderType,
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


            val dependencies = TelegramCatBotDeps(
                bot = telegramBot,
                cachedGifsChatId = ChatId(cachedGifsChatId),
                catgifsProvider = catgifsProvider,
                stringsProvider = DefaultStringsProvider,
                storage = DatabaseStorage(
                    url = databaseUrl,
                    user = databaseUser,
                    password = databasePassword
                ),
                logger = BotLogger { _, message -> logWriter.log(message) },
                scope = safeScope,
                nyanCatFile = createNyanCatFile()
            )

            startNewInstance(dependencies)
        },
        logWriter = logWriter
    )
}
