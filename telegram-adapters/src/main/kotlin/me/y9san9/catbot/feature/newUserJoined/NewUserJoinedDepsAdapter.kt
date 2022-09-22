package me.y9san9.catbot.feature.newUserJoined

import dev.inmo.tgbotapi.extensions.api.send.media.sendAnimation
import dev.inmo.tgbotapi.extensions.api.send.media.sendVideo
import dev.inmo.tgbotapi.requests.abstracts.MultipartFile
import dev.inmo.tgbotapi.types.chat.Chat
import dev.inmo.tgbotapi.types.chat.member.ChatMember
import dev.inmo.tgbotapi.types.message.textsources.TextSourcesList
import dev.inmo.tgbotapi.types.message.textsources.mention
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.y9san9.catbot.TelegramCatBotDeps
import me.y9san9.catbot.di.catgifs.CatGifsProvider
import me.y9san9.catbot.extension.bot.sendAnimationCached
import me.y9san9.catbot.feature.common.LogFoldable
import me.y9san9.catbot.feature.common.logFoldable
import me.y9san9.catbot.log.LogEvent
import java.io.File


context(TelegramCatBotDeps)
fun createNewUserJoinedDeps(): NewUserJoinedDeps = object : NewUserJoinedDeps,
    CatGifsProvider by catgifsProvider {

    override val scope: CoroutineScope =
        this@TelegramCatBotDeps.scope

    override val events: Flow<NewUserJoinedDeps.Event> =
        newUsersJoinedEvents.map { (chat, member) -> EventWrapper(chat, member) }

    inner class EventWrapper(
        private val chat: Chat,
        private val member: ChatMember
    ) : NewUserJoinedDeps.Event {
        override suspend fun isUserJoinedFirstTime(): Boolean =
            storage.isUserJoinedFirstTime(chat.id.chatId, member.user.id.chatId)

        override fun logNewMemberJoined() =
            logger.processEvent(LogEvent.ChatMemberJoined.New(chat, member))

        override suspend fun saveUserJoined() =
            storage.saveUserJoined(chat.id.chatId, member.user.id.chatId)

        override fun logUserSaved() = logger.processEvent(LogEvent.UserSaved)

        override suspend fun sendNewMemberCaptchaMessage(gifResult: Result<File>): LogFoldable {
            val mention = member.user.mention(member.user.firstName)
            val text = getStringsForChat(chat)
                .newMemberCaptchaMessage(mention)
            return sendCaptchaMessage(gifResult, text)
        }

        override fun logOldMemberJoined() =
            logger.processEvent(LogEvent.ChatMemberJoined.Old(chat, member))

        override suspend fun sendOldMemberCaptchaMessage(gifResult: Result<File>): LogFoldable {
            val mention = member.user.mention(member.user.firstName)
            val text = getStringsForChat(chat)
                .oldMemberCaptchaMessage(mention)
            return sendCaptchaMessage(gifResult, text)
        }

        private suspend fun sendCaptchaMessage(
            gifResult: Result<File>,
            message: TextSourcesList
        ) = runCatching {
            bot.sendAnimationCached(
                chat = chat,
                animation = gifResult.getOrThrow(),
                entities = message
            )
        }.logFoldable(
            success = { logger.processEvent(LogEvent.WelcomeGifSent) },
            failure = { logger.processEvent(LogEvent.CouldNotSendTheGif(chat)) }
        )
    }
}
