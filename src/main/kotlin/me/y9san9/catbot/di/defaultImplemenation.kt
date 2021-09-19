package me.y9san9.catbot.di

import me.y9san9.catbot.CatBot
import me.y9san9.catbot.di.catgifs.KtorCatgifsProvider
import me.y9san9.catbot.di.requests.TelegramRequestsExecutor

fun CatBot.Companion.defaultImplementation(token: String) = CatBot(
    requestsExecutor = TelegramRequestsExecutor(token),
    catGifs = KtorCatgifsProvider
)
