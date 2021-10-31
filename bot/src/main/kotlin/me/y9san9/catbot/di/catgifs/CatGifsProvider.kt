package me.y9san9.catbot.di.catgifs

import kotlinx.coroutines.flow.Flow

interface CatGifsProvider {
    suspend fun randomGif(): Flow<ByteArray>
}
