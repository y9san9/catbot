package me.y9san9.catbot.di.catgifs

import me.y9san9.catgifs.CatGifsClient

object KtorCatgifsProvider : BufferedCatGifsProvider {
    private val client = CatGifsClient()
    override suspend fun randomBufferedGif() = client.randomBufferedGif()
}
