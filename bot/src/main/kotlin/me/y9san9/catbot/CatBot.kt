package me.y9san9.catbot

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import me.y9san9.catbot.di.catgifs.CatGifsProvider
import me.y9san9.catbot.di.log.LogEvent
import me.y9san9.catbot.di.log.Logger
import me.y9san9.catbot.di.requests.CatbotRequestsExecutor
import me.y9san9.catbot.di.resources.StringsProvider
import me.y9san9.catbot.di.storage.Storage
import me.y9san9.catbot.handlers.handleBotJoinedToGroup
import me.y9san9.catbot.handlers.handleNewUsersJoined
import me.y9san9.catbot.handlers.handleStartCommand

object CatBot {
    fun startNewInstance(
        executor: CatbotRequestsExecutor,
        catGifs: CatGifsProvider,
        stringsProvider: StringsProvider,
        storage: Storage,
        logger: Logger,
        scope: CoroutineScope,
    ) = scope.launch {
        logger.processEvent(LogEvent.BotStarted)

        storage.init()

        handleNewUsersJoined(
            executor, stringsProvider, catGifs, storage, logger,
            scope = this
        )
        handleBotJoinedToGroup(
            executor, stringsProvider, catGifs, logger,
            scope = this
        )
        handleStartCommand(
            executor, stringsProvider, catGifs, logger,
            scope = this
        )

        logger.processEvent(LogEvent.SetupFinished)
    }
}
