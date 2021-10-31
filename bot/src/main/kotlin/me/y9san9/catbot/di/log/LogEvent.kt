package me.y9san9.catbot.di.log

import me.y9san9.catbot.di.requests.models.Chat
import me.y9san9.catbot.di.requests.models.ChatMember

sealed class LogEvent {
    object BotStarted : LogEvent()
    object SetupFinished : LogEvent()

    sealed class ChatMemberJoined(val member: ChatMember) : LogEvent() {
        class New(member: ChatMember) : ChatMemberJoined(member)
        class Old(member: ChatMember) : ChatMemberJoined(member)
    }

    object UserSaved : LogEvent()
    object GifSent : LogEvent()

    class BotJoinedToGroup(val chat: Chat) : LogEvent()
    object GroupWelcomeGifSent : LogEvent()

    object StartCommandReceived : LogEvent()
    object StartCommandGifSent : LogEvent()
}
