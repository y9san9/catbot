package me.y9san9.catbot.di.catgifs

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import me.y9san9.catbot.di.log.FromCacheGifsLogger
import me.y9san9.catgifs.cached.CatGifsClient
import me.y9san9.catgifs.cached.di.log.LogEvent
import java.io.File

class FromCacheCatgifsProvider(scope: CoroutineScope, logAction: (LogEvent, String) -> Unit) : CatGifsProvider {
    private val gifsFlow: Flow<File> = CatGifsClient(FromCacheGifsLogger(logAction)).randomGifFiles
    private val channel: Channel<File> = Channel()

    init {
        gifsFlow.onEach(channel::send)
            .launchIn(scope)
    }

    override suspend fun readRandomGifToFile(): Result<File> = Result.success(channel.receive())
}
