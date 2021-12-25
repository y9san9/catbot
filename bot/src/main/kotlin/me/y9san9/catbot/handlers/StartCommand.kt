package me.y9san9.catbot.handlers

import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import me.y9san9.catbot.CatBotDependencies
import me.y9san9.catbot.di.log.LogEvent
import me.y9san9.catbot.di.requests.context.replyWithGif

fun handleStartCommand(dependencies: CatBotDependencies) = with(dependencies) {
    executor.startCommands.onEach { message ->
        logger.processEvent(LogEvent.StartCommandReceived(message.chat))
        val gifSent = message.replyWithGif(
            entities = stringsProvider.default.startMessage(),
            gif = catGifs.readRandomGifToFile()
        )
        logger.processEvent(
            when (gifSent) {
                true -> LogEvent.StartCommandGifSent(message.chat)
                false -> LogEvent.CouldNotSentTheGif(message.chat)
            }
        )
    }.launchIn(scope)
}
