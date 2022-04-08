package me.y9san9.catbot.handlers

import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import me.y9san9.catbot.CatBotDependencies
import me.y9san9.catbot.di.log.LogEvent
import me.y9san9.catbot.di.requests.context.CatbotContext


fun handleInlineRequests(executor: CatbotContext.HasInlineRequests, dependencies: CatBotDependencies) =
    with(dependencies) {
        executor.inlineRequests.onEach { request ->
            logger.processEvent(LogEvent.InlineRequestReceived(request.from))
            val gifSent = request.answerWithGif(
                gif = catGifs.readRandomGifToFile()
            )
            request.answer()
            logger.processEvent(
                when (gifSent) {
                    true -> LogEvent.InlineRequestGifAnswered(request.from)
                    false -> LogEvent.CouldNotAnswerInlineRequest(request.from)
                }
            )
        }.launchIn(scope)
    }
