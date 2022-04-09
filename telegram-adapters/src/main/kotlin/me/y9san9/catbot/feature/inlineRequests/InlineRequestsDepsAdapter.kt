package me.y9san9.catbot.feature.inlineRequests

import dev.inmo.tgbotapi.extensions.api.answers.answerInlineQuery
import dev.inmo.tgbotapi.types.InlineQueries.InlineQueryResult.InlineQueryResultGifCachedImpl
import dev.inmo.tgbotapi.types.InlineQueries.query.InlineQuery
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.y9san9.catbot.TelegramCatBotDeps
import me.y9san9.catbot.feature.common.GifCommandDeps
import me.y9san9.catbot.feature.common.logFoldable
import me.y9san9.catbot.log.LogEvent
import java.io.File

context(TelegramCatBotDeps)
fun createInlineRequestsDeps(): InlineRequestsDeps = object : InlineRequestsDeps {
    override val scope: CoroutineScope = this@TelegramCatBotDeps.scope

    override val events: Flow<GifCommandDeps.Event> =
        inlineRequests.map(::EventWrapper)

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
                        id = "0",
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
}
