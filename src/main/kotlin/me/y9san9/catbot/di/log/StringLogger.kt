package me.y9san9.catbot.di.log

abstract class StringLogger : Logger {
    final override fun processEvent(event: LogEvent) {
        val string = when (event) {
            is LogEvent.BotStarted -> "Bot started"
            is LogEvent.SetupFinished -> "Bot setup finished"
            is LogEvent.ChatMemberJoined.New ->
                "New chat member ${event.member.title} (${event.member.link}) joined in chat ${event.member.chat.title} (${event.member.chat.link})"
            is LogEvent.ChatMemberJoined.Old ->
                "Old chat member ${event.member.title} (${event.member.link}) joined again in chat ${event.member.chat.title} (${event.member.chat.link})"
            is LogEvent.UserSaved -> "User saved to database"
            is LogEvent.GifSent -> "Gif with a cute cat sent!"
            is LogEvent.BotJoinedToGroup -> "Bot joined the group ${event.chat.title}"
            is LogEvent.GroupWelcomeGifSent -> "Group welcome gif sent"
            is LogEvent.StartCommandReceived -> "Start command received"
            is LogEvent.StartCommandGifSent -> "Start command gif sent"
        }
        processEvent(event, string)
    }

    abstract fun processEvent(event: LogEvent, stringRepresentation: String)
}
