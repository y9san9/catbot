package me.y9san9.catbot

import me.y9san9.catbot.di.catgifs.BufferedCatGifsProvider
import me.y9san9.catbot.di.requests.RequestsExecutor

class CatBot(
    requestsExecutor: RequestsExecutor,
    private val catGifs: BufferedCatGifsProvider
//    storage: Storage
) {
    private val bot = requestsExecutor

    suspend fun startNewInstance() {

    }

    companion object
}
