package me.y9san9.catbot.feature.common

import kotlinx.coroutines.flow.Flow
import me.y9san9.catbot.di.CoroutineScopeDeps
import java.io.File

interface GifCommandDeps : CoroutineScopeDeps {
    val events: Flow<Event>
    suspend fun gifFile(): Result<File>

    interface Event {
        fun logCommandReceived()
        suspend fun answerCommand(gif: Result<File>): LogFoldable
    }
}

context(GifCommandDeps)
internal fun handleGifCommands() {
    events.launchEach {
        logCommandReceived()
        val file = gifFile()
        val result = answerCommand(file)
        result.fold()
    }
}
