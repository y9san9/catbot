package me.y9san9.catbot.feature.botJoinedToGroup

import dev.inmo.tgbotapi.extensions.api.send.media.sendAnimation
import dev.inmo.tgbotapi.requests.abstracts.MultipartFile
import dev.inmo.tgbotapi.types.chat.abstracts.PublicChat
import dev.inmo.tgbotapi.utils.StorageFile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.y9san9.catbot.TelegramCatBotDeps
import me.y9san9.catbot.di.catgifs.CatGifsProvider
import me.y9san9.catbot.extension.bot.sendAnimationCached
import me.y9san9.catbot.log.LogEvent
import me.y9san9.catbot.feature.common.LogFoldable
import me.y9san9.catbot.feature.common.logFoldable
import java.io.File

context(TelegramCatBotDeps)
fun createBotJoinedToGroupDeps(): BotJoinedToGroupDeps = object : BotJoinedToGroupDeps,
    CatGifsProvider by catgifsProvider {
    override val scope: CoroutineScope =
        this@TelegramCatBotDeps.scope
    override val events: Flow<BotJoinedToGroupDeps.Event> =
        botJoinedEvents.map(::EventWrapper)

    inner class EventWrapper(private val chat: PublicChat) : BotJoinedToGroupDeps.Event {
        override fun logBotJoined() = logger.processEvent(LogEvent.BotJoinedToGroup(chat))

        override suspend fun sendWelcomeGif(gifResult: Result<File>) = runCatching {
            val gif = gifResult.getOrThrow()
            val message = getStringsForChat(chat).groupWelcomeMessage(chat)
            bot.sendAnimationCached(
                chat = chat,
                animation = gif,
                entities = message
            )
            return@runCatching gif
        }.logFoldable(
            success = { logger.processEvent(LogEvent.GroupWelcomeGifSent(it)) },
            failure = { logger.processEvent(LogEvent.CouldNotSendTheGif(chat)) }
        )
    }
}
