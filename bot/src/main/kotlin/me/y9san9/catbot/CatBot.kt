package me.y9san9.catbot

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import me.y9san9.catbot.di.catgifs.CatGifsProvider
import me.y9san9.catbot.di.log.LogEvent
import me.y9san9.catbot.di.log.Logger
import me.y9san9.catbot.di.requests.CatbotRequestsExecutor
import me.y9san9.catbot.di.resources.StringsProvider
import me.y9san9.catbot.di.storage.Storage
import me.y9san9.catbot.handlers.handleBotJoinedToGroup
import me.y9san9.catbot.handlers.handleNewUsersJoined
import me.y9san9.catbot.handlers.handleStartCommand

class CatBot(
    executor: CatbotRequestsExecutor,
    private val catGifs: CatGifsProvider,
    private val stringsProvider: StringsProvider,
    private val storage: Storage,
    private val logger: Logger,
    private val scope: CoroutineScope
) {
    private val bot = executor

    suspend fun startNewInstance() {
        logger.processEvent(LogEvent.BotStarted)

        storage.init()

        handleNewUsersJoined(
            executor = bot,
            stringsProvider, catGifs, storage, logger, scope
        )
        handleBotJoinedToGroup(
            executor = bot,
            stringsProvider, catGifs, logger, scope
        )
        handleStartCommand(
            executor = bot,
            stringsProvider, catGifs, logger, scope
        )

        logger.processEvent(LogEvent.SetupFinished)
    }

    companion object
}
