package me.y9san9.catbot.handlers

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import me.y9san9.catbot.di.catgifs.CatGifsProvider
import me.y9san9.catbot.di.log.LogEvent
import me.y9san9.catbot.di.log.Logger
import me.y9san9.catbot.di.requests.CatbotRequestsExecutor
import me.y9san9.catbot.di.resources.StringsProvider

fun handleBotJoinedToGroup(
    executor: CatbotRequestsExecutor,
    stringsProvider: StringsProvider,
    catGifs: CatGifsProvider,
    logger: Logger,
    scope: CoroutineScope
) {
    executor.botJoinedToGroup.onEach { chat ->
        logger.processEvent(LogEvent.BotJoinedToGroup(chat))
        executor.sendGif(
            chatId = chat.id,
            text = stringsProvider.default.groupWelcomeMessage(chat.title),
            gif = catGifs.readRandomGifToFile()
        )
        logger.processEvent(LogEvent.GroupWelcomeGifSent)

    }.launchIn(scope)
}
