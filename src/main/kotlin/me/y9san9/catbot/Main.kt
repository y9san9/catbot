package me.y9san9.catbot

import me.y9san9.catbot.di.defaultImplementation

suspend fun main() {
    val botToken = getEnvOrFail("BOT_TOKEN")

    CatBot
        .defaultImplementation(botToken)
        .startNewInstance()
}
