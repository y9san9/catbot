package me.y9san9.catbot.di.catgifs

import kotlinx.coroutines.flow.Flow

interface BufferedCatGifsProvider {
    suspend fun randomBufferedGif(): Flow<ByteArray>
}
