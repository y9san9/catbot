package me.y9san9.catbot.log

import dev.inmo.tgbotapi.types.chat.Chat
import dev.inmo.tgbotapi.types.chat.PublicChat
import dev.inmo.tgbotapi.types.chat.member.ChatMember
import java.io.File

sealed class LogEvent {
    object BotStarted : LogEvent()
    object SetupFinished : LogEvent()

    data class BotJoinedToGroup(val chat: PublicChat) : LogEvent()
    data class GroupWelcomeGifSent(val file: File) : LogEvent()
    data class CouldNotSendTheGif(val chat: Chat) : LogEvent()

    sealed class ChatMemberJoined(val chat: Chat, val member: ChatMember) : LogEvent() {
        class New(chat: Chat, member: ChatMember) : ChatMemberJoined(chat, member)
        class Old(chat: Chat, member: ChatMember) : ChatMemberJoined(chat, member)
    }

    object UserSaved : LogEvent()
    object WelcomeGifSent : LogEvent()

    class StartCommandReceived(val chat: Chat) : LogEvent()
    class StartCommandGifSent(val chat: Chat) : LogEvent()

    class InlineRequestReceived(val chat: Chat) : LogEvent()
    class InlineRequestGifAnswered(val chat: Chat) : LogEvent()
    class CouldNotAnswerInlineRequest(val chat: Chat) : LogEvent()

    class NyanCatReceived(val chat: Chat) : LogEvent()
    class NyanCatGifAnswered(val chat: Chat) : LogEvent()
}
