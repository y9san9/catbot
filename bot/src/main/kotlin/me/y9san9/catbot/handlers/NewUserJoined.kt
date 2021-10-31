package me.y9san9.catbot.handlers

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import me.y9san9.catbot.di.catgifs.CatGifsProvider
import me.y9san9.catbot.di.catgifs.readRandomGifToFile
import me.y9san9.catbot.di.log.LogEvent
import me.y9san9.catbot.di.log.Logger
import me.y9san9.catbot.di.requests.CatbotRequestsExecutor
import me.y9san9.catbot.di.resources.StringsProvider
import me.y9san9.catbot.di.storage.Storage
import me.y9san9.catbot.util.unit

fun handleNewUsersJoined(
    executor: CatbotRequestsExecutor,
    stringsProvider: StringsProvider,
    catGifs: CatGifsProvider,
    storage: Storage,
    logger: Logger,
    scope: CoroutineScope
) {
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
        executor.sendGif(
            chatId = member.chat.id,
            text = text,
            gif = catGifs.readRandomGifToFile()
        )
        logger.processEvent(LogEvent.GifSent)
    }.launchIn(scope)
}
