package me.y9san9.catbot

import kotlinx.coroutines.coroutineScope
import me.y9san9.catbot.di.defaultImplementation

suspend fun main() = coroutineScope {
    val botToken = getEnvOrFail("BOT_TOKEN")

    CatBot
        .defaultImplementation(
            scope = this,
            token = botToken
        ).startNewInstance()
}
