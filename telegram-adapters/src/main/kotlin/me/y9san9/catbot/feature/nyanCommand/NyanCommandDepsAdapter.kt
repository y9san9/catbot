package me.y9san9.catbot.feature.nyanCommand

import dev.inmo.tgbotapi.extensions.api.send.media.sendAnimation
import dev.inmo.tgbotapi.requests.abstracts.MultipartFile
import dev.inmo.tgbotapi.types.message.abstracts.Message
import dev.inmo.tgbotapi.types.message.abstracts.PossiblyReplyMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.y9san9.catbot.TelegramCatBotDeps
import me.y9san9.catbot.extension.bot.sendAnimationCached
import me.y9san9.catbot.feature.common.GifCommandDeps
import me.y9san9.catbot.feature.common.logFoldable
import me.y9san9.catbot.log.LogEvent
import java.io.File

context(TelegramCatBotDeps)
fun createNyanCommandDeps(): NyanCommandDeps = object : NyanCommandDeps {
    override val scope: CoroutineScope = this@TelegramCatBotDeps.scope
    override val events: Flow<GifCommandDeps.Event> = nyanCommands.map(::EventWrapper)

    override suspend fun gifFile(): Result<File> = Result.success(nyanCatFile)

    inner class EventWrapper(val message: Message) : GifCommandDeps.Event {

        override fun logCommandReceived() =
            logger.processEvent(LogEvent.NyanCatReceived(message.chat))

        override suspend fun answerCommand(gif: Result<File>) = runCatching {
            bot.sendAnimationCached(
                chat = message.chat,
                animation = gif.getOrThrow(),
                replyToMessageId = ((message as? PossiblyReplyMessage)?.replyTo ?: message).messageId
            )
        }.logFoldable(
            success = { logger.processEvent(LogEvent.NyanCatGifAnswered(message.chat)) },
            failure = { logger.processEvent(LogEvent.CouldNotSendTheGif(message.chat)) }
        )
    }
}
