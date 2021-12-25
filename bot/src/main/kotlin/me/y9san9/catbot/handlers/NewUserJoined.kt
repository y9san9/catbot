package me.y9san9.catbot.handlers

import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import me.y9san9.catbot.CatBotDependencies
import me.y9san9.catbot.di.log.LogEvent
import me.y9san9.catbot.di.requests.context.sendGif
import me.y9san9.catbot.di.resources.get

fun handleNewUsersJoined(dependencies: CatBotDependencies) = with(dependencies) {
    executor.newMembersJoined.onEach { member ->
        val text = when (member.isJoinedFirstTime()) {
            true -> {
                logger.processEvent(LogEvent.ChatMemberJoined.New(member))
                member.saveJoined()
                logger.processEvent(LogEvent.UserSaved)
                stringsProvider[member].newMemberCaptchaMessage(member.mention)
            }
            false -> {
                logger.processEvent(LogEvent.ChatMemberJoined.Old(member))
                stringsProvider[member].oldMemberCaptchaMessage(member.mention)
            }
        }
        val gifSent = member.chat.sendGif(
            entities = text,
            gif = catGifs.readRandomGifToFile()
        )
        logger.processEvent(if (gifSent) LogEvent.WelcomeGifSent else LogEvent.CouldNotSentTheGif(member.chat))
    }.launchIn(scope)
}
