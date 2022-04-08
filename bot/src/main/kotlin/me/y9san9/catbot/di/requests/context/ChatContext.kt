package me.y9san9.catbot.di.requests.context

import me.y9san9.catbot.di.requests.models.TextEntities
import me.y9san9.catbot.di.requests.models.TextEntity
import me.y9san9.catbot.di.requests.models.raw
import java.io.File

sealed interface ChatContext {
    val title: String
    val link: String?
    val languageCode: String?

    suspend fun sendGif(text: String, gif: File): Boolean

    sealed interface WithEntities : ChatContext {
        override suspend fun sendGif(text: String, gif: File) = sendGif(TextEntity.Regular(text).asList, gif)
        suspend fun sendGif(entities: TextEntities, gif: File): Boolean
    }

    sealed interface WithLongId : ChatContext {
        val id: Long
    }

    interface Telegram : WithEntities, WithLongId
}

suspend fun ChatContext.sendGif(entities: TextEntities, gif: File) = when (this) {
    is ChatContext.WithEntities -> sendGif(entities, gif)
    else -> sendGif(entities.raw, gif)
}
