package me.y9san9.catbot.di.catgifs

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.plus
import me.y9san9.catbot.di.log.GifsLogger
import me.y9san9.catgifs.CatGifsClient
import me.y9san9.catgifs.di.log.LogEvent
import java.io.File

class KtorCatgifsProvider(scope: CoroutineScope, logAction: (LogEvent, String) -> Unit) : CatGifsProvider {

    private val gifsFlow = CatGifsClient(GifsLogger(logAction))
        .randomGifFiles()

    // files channel
    private val channel: Channel<File> = Channel()

    init {
        gifsFlow.onEach(channel::send)
            .launchIn(scope)
    }

    override suspend fun readRandomGifToFile(): File = channel.receive()
}
