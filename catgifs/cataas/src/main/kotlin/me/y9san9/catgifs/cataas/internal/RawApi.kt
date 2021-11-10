package me.y9san9.catgifs.cataas.internal

import io.ktor.client.HttpClient
import io.ktor.client.features.BrowserUserAgent
import io.ktor.client.request.get
import io.ktor.utils.io.ByteReadChannel

internal object RawApi {
    private const val endpoint = "https://cataas.com/cat/gif"

    private val client = HttpClient {
        BrowserUserAgent()
    }

    suspend fun randomGifStream(): ByteReadChannel = client.get(endpoint)
}
