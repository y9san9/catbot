package me.y9san9.catbot.handlers

import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import me.y9san9.catbot.CatBotDependencies
import me.y9san9.catbot.di.log.LogEvent

fun handleStartCommand(dependencies: CatBotDependencies) = with(dependencies) {
    executor.startCommands.onEach { chat ->
        logger.processEvent(LogEvent.StartCommandReceived(chat))
        val gifSent = executor.sendGif(
            chat = chat,
            text = stringsProvider.default.startMessage(),
            gif = catGifs.readRandomGifToFile()
        )
        logger.processEvent(if (gifSent) LogEvent.StartCommandGifSent(chat) else LogEvent.CouldNotSentTheGif(chat))
    }.launchIn(scope)
}
