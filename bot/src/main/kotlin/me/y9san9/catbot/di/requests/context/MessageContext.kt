package me.y9san9.catbot.di.requests.context

import me.y9san9.catbot.di.requests.models.TextEntities
import me.y9san9.catbot.di.requests.models.TextEntity
import me.y9san9.catbot.di.requests.models.raw
import java.io.File

sealed interface MessageContext {
    val chat: ChatContext
    val replyMessage: MessageContext?

    suspend fun replyWithGif(text: String, gif: File): Boolean

    sealed interface WithEntities : MessageContext {
        override suspend fun replyWithGif(text: String, gif: File) = replyWithGif(TextEntity.Regular(text).asList, gif)
        suspend fun replyWithGif(entities: TextEntities, gif: File): Boolean
    }

    interface Telegram : WithEntities {
        override val chat: ChatContext.Telegram
        @Suppress("RemoveRedundantQualifierName")
        override val replyMessage: MessageContext.Telegram?
    }
}

@Suppress("REDUNDANT_ELSE_IN_WHEN")
suspend fun MessageContext.replyWithGif(entities: TextEntities, gif: File) = when (this) {
    is MessageContext.WithEntities -> replyWithGif(entities, gif)
    else -> replyWithGif(entities.raw, gif)
}
