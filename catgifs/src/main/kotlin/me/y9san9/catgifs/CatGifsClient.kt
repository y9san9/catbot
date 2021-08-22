package me.y9san9.catgifs

import me.y9san9.catgifs.internal.RawApi
import me.y9san9.catgifs.internal.extensions.byteReadChannel.readAsFlow

class CatGifsClient {
    suspend fun bufferedGifFlow(bufferSize: Int = DEFAULT_BUFFER_SIZE) = RawApi
        .randomGifStream()
        .readAsFlow(bufferSize)
}
