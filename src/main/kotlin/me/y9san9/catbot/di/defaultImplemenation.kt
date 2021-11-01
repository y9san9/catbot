package me.y9san9.catbot.di

import dev.inmo.tgbotapi.bot.Ktor.telegramBot
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.newCoroutineContext
import kotlinx.coroutines.plus
import me.y9san9.catbot.CatBot
import me.y9san9.catbot.di.catgifs.KtorCatgifsProvider
import me.y9san9.catbot.di.log.BotLogger
import me.y9san9.catbot.di.log.PrintLogger
import me.y9san9.catbot.di.log.TelegramLogger
import me.y9san9.catbot.di.requests.TelegramRequestsExecutor
import me.y9san9.catbot.di.resources.DefaultStringsProvider
import me.y9san9.catbot.di.storage.db.DatabaseStorage

fun CatBot.startNewDefaultInstanceSafely(
    scope: CoroutineScope,
    token: String,
    logChatId: Long?,
    databaseUrl: String,
    databaseUser: String,
    databasePassword: String
) {
    val telegramBot = telegramBot(token)
    val logWriter = when (logChatId) {
        null -> PrintLogger
        else -> TelegramLogger(logChatId, telegramBot, scope)
    }

    startNewInstanceSafely(
        scope = scope,
        botStarter = { safeScope ->
            startNewInstance(
                executor = TelegramRequestsExecutor(safeScope, telegramBot),
                catGifs = KtorCatgifsProvider(safeScope) { _, message -> logWriter.log(message) },
                stringsProvider = DefaultStringsProvider,
                storage = DatabaseStorage(
                    url = databaseUrl,
                    user = databaseUser,
                    password = databasePassword
                ),
                logger = BotLogger { _, message -> logWriter.log(message) },
                scope = safeScope
            )
        },
        logWriter = logWriter
    )
}
