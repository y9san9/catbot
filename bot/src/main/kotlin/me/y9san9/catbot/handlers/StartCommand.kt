package me.y9san9.catbot.handlers

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import me.y9san9.catbot.di.catgifs.CatGifsProvider
import me.y9san9.catbot.di.log.LogEvent
import me.y9san9.catbot.di.log.Logger
import me.y9san9.catbot.di.requests.CatbotRequestsExecutor
import me.y9san9.catbot.di.resources.StringsProvider

fun handleStartCommand(
    executor: CatbotRequestsExecutor,
    stringsProvider: StringsProvider,
    catGifs: CatGifsProvider,
    logger: Logger,
    scope: CoroutineScope
) {
    executor.startCommands.onEach { chat ->
        logger.processEvent(LogEvent.StartCommandReceived(chat))
        val gifSent = executor.sendGif(
            chatId = chat.id,
            text = stringsProvider.default.startMessage(),
            gif = catGifs.readRandomGifToFile()
        )
        logger.processEvent(if (gifSent) LogEvent.StartCommandGifSent(chat) else LogEvent.CouldNotSentTheGif(chat))
    }.launchIn(scope)
}
