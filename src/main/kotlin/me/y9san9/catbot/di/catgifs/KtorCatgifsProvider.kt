package me.y9san9.catbot.di.catgifs

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.plus
import me.y9san9.catbot.di.log.GifsPrintLogger
import me.y9san9.catgifs.CatGifsClient
import java.io.File

object KtorCatgifsProvider : CatGifsProvider {
    private val scope = CoroutineScope(Dispatchers.Default) + CoroutineName(name = "Cat Gifs Coroutine")

    private val gifsFlow = CatGifsClient(GifsPrintLogger)
        .randomGifFiles()

    // files channel
    private val channel: Channel<File> = Channel()

    init {
        gifsFlow.onEach(channel::send)
            .launchIn(scope)
    }

    override suspend fun readRandomGifToFile(): File = channel.receive()
}
