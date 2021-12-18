package me.y9san9.catbot

import kotlinx.coroutines.launch
import me.y9san9.catbot.di.log.LogEvent
import me.y9san9.catbot.handlers.handleBotJoinedToGroup
import me.y9san9.catbot.handlers.handleNewUsersJoined
import me.y9san9.catbot.handlers.handleStartCommand

object CatBot {
    fun startNewInstance(dependencies: CatBotDependencies) = dependencies.scope.launch {
        dependencies.logger.processEvent(LogEvent.BotStarted)

        dependencies.storage.init()

        handleNewUsersJoined(dependencies)
        handleBotJoinedToGroup(dependencies)
        handleStartCommand(dependencies)

        dependencies.logger.processEvent(LogEvent.SetupFinished)
    }
}
