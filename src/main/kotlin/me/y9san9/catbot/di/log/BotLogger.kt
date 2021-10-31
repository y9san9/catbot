package me.y9san9.catbot.di.log

class BotLogger(
    private val logAction: (LogEvent, String) -> Unit
) : Logger {
    override fun processEvent(event: LogEvent) {
        val string = when (event) {
            is LogEvent.BotStarted -> "Bot started"
            is LogEvent.SetupFinished -> "Bot setup finished"
            is LogEvent.ChatMemberJoined.New ->
                "New chat member ${event.member.title} (${event.member.link}) joined in chat ${event.member.chat.title} (${event.member.chat.link})"
            is LogEvent.ChatMemberJoined.Old ->
                "Old chat member ${event.member.title} (${event.member.link}) joined again in chat ${event.member.chat.title} (${event.member.chat.link})"
            is LogEvent.UserSaved -> "User saved to database"
            is LogEvent.GifSent -> "Welcome gif with a cute cat sent!"
            is LogEvent.BotJoinedToGroup -> "Bot joined the group ${event.chat.title} (${event.chat.link}"
            is LogEvent.GroupWelcomeGifSent -> "Group welcome gif sent"
            is LogEvent.StartCommandReceived -> "Start command received in chat ${event.chat.title} (${event.chat.link})"
            is LogEvent.StartCommandGifSent -> "Start command gif sent in chat ${event.chat.title} (${event.chat.link})"
        }
        logAction(event, string)
    }
}
