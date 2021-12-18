package me.y9san9.catbot.handlers

import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import me.y9san9.catbot.CatBotDependencies
import me.y9san9.catbot.di.log.LogEvent

fun handleNewUsersJoined(dependencies: CatBotDependencies) = with(dependencies) {
    executor.newMembersJoined.onEach { member ->
        val text = when (storage.isUserJoinedFirstTime(member)) {
            true -> {
                logger.processEvent(LogEvent.ChatMemberJoined.New(member))
                storage.saveUserJoined(member)
                logger.processEvent(LogEvent.UserSaved)
                stringsProvider[member.languageCode].newMemberCaptchaMessage(member.mention)
            }
            false -> {
                logger.processEvent(LogEvent.ChatMemberJoined.Old(member))
                stringsProvider[member.languageCode].oldMemberCaptchaMessage(member.mention)
            }
        }
        val gifSent = executor.sendGif(
            chat = member.chat,
            text = text,
            gif = catGifs.readRandomGifToFile()
        )
        logger.processEvent(if (gifSent) LogEvent.WelcomeGifSent else LogEvent.CouldNotSentTheGif(member.chat))
    }.launchIn(scope)
}
