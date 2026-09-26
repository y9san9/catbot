package me.y9san9.catbot.feature.inlineRequests

import dev.inmo.tgbotapi.extensions.api.answers.answerGuestQuery
import dev.inmo.tgbotapi.extensions.api.answers.answerInlineQuery
import dev.inmo.tgbotapi.types.InlineQueries.InlineQueryResult.InlineQueryResultGifCachedImpl
import dev.inmo.tgbotapi.types.InlineQueries.query.InlineQuery
import dev.inmo.tgbotapi.types.InlineQueryId
import dev.inmo.tgbotapi.types.message.abstracts.RequestGuestContentMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import me.y9san9.catbot.TelegramCatBotDeps
import me.y9san9.catbot.feature.common.GifCommandDeps
import me.y9san9.catbot.feature.common.logFoldable
import me.y9san9.catbot.log.LogEvent
import java.io.File

context(deps: TelegramCatBotDeps)
fun createInlineRequestsDeps(): InlineRequestsDeps = with(deps) {
    object : InlineRequestsDeps {
        override val scope: CoroutineScope = deps.scope

        override val events: Flow<GifCommandDeps.Event> =
            merge(
                inlineRequests.map(::EventWrapper),
                guestRequests.map(::GuestEventWrapper)
            )

        override suspend fun gifFile(): Result<File> =
            catgifsProvider.readRandomGifToFile()

        inner class EventWrapper(private val request: InlineQuery) : GifCommandDeps.Event {
            override fun logCommandReceived() =
                logger.processEvent(LogEvent.InlineRequestReceived(request.from))

            override suspend fun answerCommand(gif: Result<File>) = runCatching {
                val fileId = catGifFileIdRepository.getRandomFileId().getOrThrow()

                bot.answerInlineQuery(
                    inlineQuery = request,
                    results = listOf(
                    InlineQueryResultGifCachedImpl(
                        id = InlineQueryId("0"),
                        fileId = fileId
                    )
                    ),
                    cachedTime = 0,
                    isPersonal = false
                )
            }.logFoldable(
                success = { logger.processEvent(LogEvent.InlineRequestGifAnswered(request.from)) },
                failure = { logger.processEvent(LogEvent.CouldNotAnswerInlineRequest(request.from)) }
            )
        }

        // Guest mode: the bot is mentioned in a chat it is not a member of,
        // so there is no inline query to answer, only a guest query id
        inner class GuestEventWrapper(private val request: RequestGuestContentMessage<*>) : GifCommandDeps.Event {
            override fun logCommandReceived() =
                logger.processEvent(LogEvent.InlineRequestReceived(request.from))

            override suspend fun answerCommand(gif: Result<File>) = runCatching {
                val fileId = catGifFileIdRepository.getRandomFileId().getOrThrow()

                bot.answerGuestQuery(
                    guestQueryId = request.guestQueryId,
                    result = InlineQueryResultGifCachedImpl(
                        id = InlineQueryId("0"),
                        fileId = fileId
                    )
                )
                Unit
            }.logFoldable(
                success = { logger.processEvent(LogEvent.InlineRequestGifAnswered(request.from)) },
                failure = { logger.processEvent(LogEvent.CouldNotAnswerInlineRequest(request.from)) }
            )
        }
    }
}
