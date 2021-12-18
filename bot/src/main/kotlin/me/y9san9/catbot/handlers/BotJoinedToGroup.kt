package me.y9san9.catbot.handlers

import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import me.y9san9.catbot.CatBotDependencies
import me.y9san9.catbot.di.log.LogEvent

fun handleBotJoinedToGroup(dependencies: CatBotDependencies) = with(dependencies) {
    executor.botJoinedToGroup.onEach { chat ->
        logger.processEvent(LogEvent.BotJoinedToGroup(chat))
        val gifSent = executor.sendGif(
            chat = chat,
            text = stringsProvider.default.groupWelcomeMessage(chat.title),
            gif = catGifs.readRandomGifToFile()
        )
        logger.processEvent(if (gifSent) LogEvent.GroupWelcomeGifSent else LogEvent.CouldNotSentTheGif(chat))

    }.launchIn(scope)
}
