package me.y9san9.catbot

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import me.y9san9.catbot.di.catgifs.BufferedCatGifsProvider
import me.y9san9.catbot.di.catgifs.readRandomGifToFile
import me.y9san9.catbot.di.requests.RequestsExecutor
import me.y9san9.catbot.di.requests.text.TextEntity
import me.y9san9.catbot.di.requests.text.plus
import me.y9san9.catbot.di.resources.StringsProvider

class CatBot(
    requestsExecutor: RequestsExecutor,
    private val catGifs: BufferedCatGifsProvider,
    private val stringsProvider: StringsProvider
//    storage: Storage
) {
    private val bot = requestsExecutor

    suspend fun startNewInstance() = coroutineScope {
        bot.newMembersSelfJoined.onEach { member ->
            bot.sendGif(
                chatId = member.chatId,
                text = stringsProvider[member.languageCode].newMemberCaptchaMessage(member.mention),
                gif = catGifs.readRandomGifToFile()
            )
        }.launchIn(scope = this)

        return@coroutineScope
    }

    companion object
}
