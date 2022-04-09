package me.y9san9.catbot.di.catgifs

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import me.y9san9.catbot.di.log.CataasGifsLogger
import me.y9san9.catgifs.cataas.CatGifsClient
import me.y9san9.catgifs.cataas.di.log.LogEvent
import java.io.File

class KtorCatgifsProvider(scope: CoroutineScope, logAction: (LogEvent, String) -> Unit) : CatGifsProvider {
    private val gifsFlow = CatGifsClient(CataasGifsLogger(logAction)).randomGifFiles

    // files channel
    private val channel: Channel<Result<File>> = Channel()

    init {
        gifsFlow.onEach(channel::send)
            .launchIn(scope)
    }

    override suspend fun readRandomGifToFile(): Result<File> = channel.receive()
}
