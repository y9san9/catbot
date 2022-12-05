package me.y9san9.catbot.di.log

import dev.inmo.tgbotapi.extensions.utils.formatting.link
import dev.inmo.tgbotapi.types.chat.Chat
import dev.inmo.tgbotapi.types.chat.PrivateChat
import dev.inmo.tgbotapi.types.chat.PublicChat
import dev.inmo.tgbotapi.types.link
import me.y9san9.catbot.log.LogEvent

class BotLogger(
    private val logAction: (LogEvent, String) -> Unit
) : Logger {
    override fun processEvent(event: LogEvent) {
        val string = when (event) {
            is LogEvent.BotStarted -> "Bot started"
            is LogEvent.SetupFinished -> "Bot setup finished"
            is LogEvent.ChatMemberJoined.New ->
                "New chat member ${event.member.user.title} (${event.member.user.link}) joined in chat " +
                        "${event.chat.title} (${event.chat.link})"
            is LogEvent.ChatMemberJoined.Old ->
                "Old chat member ${event.member.user.title} (${event.member.user.link}) joined again in chat " +
                        "${event.chat.title} (${event.chat.link})"
            is LogEvent.UserSaved -> "User saved to database"
            is LogEvent.WelcomeGifSent -> "Welcome gif with a cute cat sent!"
            is LogEvent.BotJoinedToGroup -> "Bot joined the group ${event.chat.title} (${event.chat.link})"
            is LogEvent.GroupWelcomeGifSent -> "Group welcome gif sent"
            is LogEvent.StartCommandReceived -> "Start command received in chat ${event.chat.title} (${event.chat.link})"
            is LogEvent.StartCommandGifSent -> "Start command gif sent in chat ${event.chat.title} (${event.chat.link})"
            is LogEvent.CouldNotSendTheGif ->
                "Could not send the gif to chat ${event.chat.title} (${event.chat.link}). "+
                    "Probably the bot was blocked by user, or removed from chat \uD83D\uDE3F"
            is LogEvent.CouldNotAnswerInlineRequest ->
                "Could not answer inline request in chat ${event.chat.title} (${event.chat.link})"
            is LogEvent.InlineRequestGifAnswered ->
                "Inline query gif sent in chat ${event.chat.title} (${event.chat.link})"
            is LogEvent.InlineRequestReceived ->
                "Inline query received in chat ${event.chat.title} (${event.chat.link})"
            is LogEvent.NyanCatGifAnswered -> "Nyan cat gif answered in chat ${event.chat.title} (${event.chat.link})"
            is LogEvent.NyanCatReceived -> "Nyan cat command received in chat ${event.chat.title} (${event.chat.link})"
        }
        logAction(event, string)
    }
}

private val PrivateChat.title get() = "$firstName${lastName.takeIf(String::isNotBlank)?.let { " $it" } ?: ""}"
private val Chat.title get() = when (this) {
    is PrivateChat -> title
    is PublicChat -> title
    else -> error("Not supported $this")
}
