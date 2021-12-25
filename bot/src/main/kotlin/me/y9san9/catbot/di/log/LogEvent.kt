package me.y9san9.catbot.di.log

import me.y9san9.catbot.di.requests.context.ChatContext
import me.y9san9.catbot.di.requests.context.ChatMemberContext

sealed class LogEvent {
    object BotStarted : LogEvent()
    object SetupFinished : LogEvent()

    sealed class ChatMemberJoined(val member: ChatMemberContext) : LogEvent() {
        class New(member: ChatMemberContext) : ChatMemberJoined(member)
        class Old(member: ChatMemberContext) : ChatMemberJoined(member)
    }

    object UserSaved : LogEvent()
    object WelcomeGifSent : LogEvent()

    class BotJoinedToGroup(val chat: ChatContext) : LogEvent()
    object GroupWelcomeGifSent : LogEvent()

    class StartCommandReceived(val chat: ChatContext) : LogEvent()
    class StartCommandGifSent(val chat: ChatContext) : LogEvent()
    class CouldNotSentTheGif(val chat: ChatContext) : LogEvent()

    class InlineRequestReceived(val chat: ChatContext) : LogEvent()
    class InlineRequestGifAnswered(val chat: ChatContext) : LogEvent()
    class CouldNotAnswerInlineRequest(val chat: ChatContext) : LogEvent()
}
