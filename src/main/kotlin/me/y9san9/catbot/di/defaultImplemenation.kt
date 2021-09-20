package me.y9san9.catbot.di

import kotlinx.coroutines.CoroutineScope
import me.y9san9.catbot.CatBot
import me.y9san9.catbot.di.catgifs.KtorCatgifsProvider
import me.y9san9.catbot.di.requests.TelegramRequestsExecutor

fun CatBot.Companion.defaultImplementation(scope: CoroutineScope, token: String) = CatBot(
    requestsExecutor = TelegramRequestsExecutor(scope, token),
    catGifs = KtorCatgifsProvider
)
