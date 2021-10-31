package me.y9san9.catbot.di

import kotlinx.coroutines.CoroutineScope
import me.y9san9.catbot.CatBot
import me.y9san9.catbot.di.catgifs.KtorCatgifsProvider
import me.y9san9.catbot.di.log.PrintLogger
import me.y9san9.catbot.di.requests.TelegramRequestsExecutor
import me.y9san9.catbot.di.resources.DefaultStringsProvider
import me.y9san9.catbot.di.storage.db.DatabaseStorage

fun CatBot.Companion.defaultImplementation(
    scope: CoroutineScope,
    token: String,
    databaseUrl: String,
    databaseUser: String,
    databasePassword: String
) = CatBot(
    executor = TelegramRequestsExecutor(token),
    catGifs = KtorCatgifsProvider,
    stringsProvider = DefaultStringsProvider,
    storage = DatabaseStorage(
        url = databaseUrl,
        user = databaseUser,
        password = databasePassword
    ),
    logger = PrintLogger,
    scope = scope
)
