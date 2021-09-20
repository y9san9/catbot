package me.y9san9.catbot

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import me.y9san9.catbot.di.catgifs.BufferedCatGifsProvider
import me.y9san9.catbot.di.catgifs.readRandomGifToFile
import me.y9san9.catbot.di.requests.RequestsExecutor
import me.y9san9.catbot.di.requests.text.TextEntity
import me.y9san9.catbot.di.requests.text.plus

class CatBot(
    requestsExecutor: RequestsExecutor,
    private val catGifs: BufferedCatGifsProvider
//    storage: Storage
) {
    private val bot = requestsExecutor

    suspend fun startNewInstance() = coroutineScope {
        bot.newMembersSelfJoined.onEach { member ->
            bot.sendGif(
                chatId = member.chatId,
                text = member.mention + ", докажи, что ты человек.\nНапиши, что происходит на картнке. У тебя пара минут \uD83D\uDE3A",
                gif = catGifs.readRandomGifToFile()
            )
        }.launchIn(scope = this)

        return@coroutineScope
    }

    companion object
}
