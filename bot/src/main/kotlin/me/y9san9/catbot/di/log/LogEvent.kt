package me.y9san9.catbot.di.log

import me.y9san9.catbot.di.requests.models.Chat
import me.y9san9.catbot.di.requests.models.ChatMember
import me.y9san9.catbot.di.requests.models.User

sealed class LogEvent {
    object BotStarted : LogEvent()
    object SetupFinished : LogEvent()

    sealed class ChatMemberJoined(val member: ChatMember) : LogEvent() {
        class New(member: ChatMember) : ChatMemberJoined(member)
        class Old(member: ChatMember) : ChatMemberJoined(member)
    }

    object UserSaved : LogEvent()
    object WelcomeGifSent : LogEvent()

    class BotJoinedToGroup(val chat: Chat) : LogEvent()
    object GroupWelcomeGifSent : LogEvent()

    class StartCommandReceived(val chat: Chat) : LogEvent()
    class StartCommandGifSent(val chat: Chat) : LogEvent()
    class CouldNotSentTheGif(val chat: Chat) : LogEvent()

    class InlineRequestReceived(val chat: Chat) : LogEvent()
    class InlineRequestGifAnswered(val chat: Chat) : LogEvent()
    class CouldNotAnswerInlineRequest(val chat: Chat) : LogEvent()
}
