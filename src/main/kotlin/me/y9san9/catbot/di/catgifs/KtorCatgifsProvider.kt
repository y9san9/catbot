package me.y9san9.catbot.di.catgifs

import me.y9san9.catgifs.CatGifsClient

object KtorCatgifsProvider : CatGifsProvider {
    private val client = CatGifsClient()
    override suspend fun randomGif() = client.randomBufferedGif()
}
