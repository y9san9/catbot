package me.y9san9.catbot

object TelegramCatBot {
    fun startNewInstance(dependencies: TelegramCatBotDeps) =
        with(dependencies.deps) { CatBot.startNewInstance() }
}
