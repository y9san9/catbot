package me.y9san9.catbot.di.requests.context

import java.io.File

sealed interface InlineRequestContext {
    val from: UserContext

    suspend fun answerWithGif(gif: File): Boolean
    suspend fun answer(): Boolean

    interface Telegram : InlineRequestContext
}
