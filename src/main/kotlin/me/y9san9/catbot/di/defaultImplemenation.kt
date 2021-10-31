package me.y9san9.catbot.di

import dev.inmo.tgbotapi.bot.Ktor.telegramBot
import dev.inmo.tgbotapi.extensions.api.telegramBot
import kotlinx.coroutines.CoroutineScope
import me.y9san9.catbot.CatBot
import me.y9san9.catbot.di.catgifs.KtorCatgifsProvider
import me.y9san9.catbot.di.log.BotLogger
import me.y9san9.catbot.di.log.PrintLogger
import me.y9san9.catbot.di.log.TelegramLogger
import me.y9san9.catbot.di.requests.TelegramRequestsExecutor
import me.y9san9.catbot.di.resources.DefaultStringsProvider
import me.y9san9.catbot.di.storage.db.DatabaseStorage
import kotlin.math.log

fun CatBot.Companion.defaultImplementation(
    scope: CoroutineScope,
    token: String,
    logChatId: Long?,
    databaseUrl: String,
    databaseUser: String,
    databasePassword: String
): CatBot {
    val telegramBot = telegramBot(token)
    val logger = when (logChatId) {
        null -> PrintLogger
        else -> TelegramLogger(logChatId, telegramBot, scope)
    }
    return CatBot(
        executor = TelegramRequestsExecutor(telegramBot),
        catGifs = KtorCatgifsProvider { _, message -> logger.log(message) },
        stringsProvider = DefaultStringsProvider,
        storage = DatabaseStorage(
            url = databaseUrl,
            user = databaseUser,
            password = databasePassword
        ),
        logger = BotLogger { _, message -> logger.log(message) },
        scope = scope
    )
}
