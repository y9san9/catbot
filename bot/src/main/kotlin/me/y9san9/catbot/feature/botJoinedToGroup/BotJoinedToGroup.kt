package me.y9san9.catbot.feature.botJoinedToGroup

import kotlinx.coroutines.flow.Flow
import me.y9san9.catbot.di.CoroutineScopeDeps
import me.y9san9.catbot.di.catgifs.CatGifsProvider
import me.y9san9.catbot.feature.common.LogFoldable
import me.y9san9.catbot.feature.common.fold
import java.io.File

interface BotJoinedToGroupDeps : CoroutineScopeDeps, CatGifsProvider {
    val events: Flow<Event>

    interface Event {
        fun logBotJoined()
        suspend fun sendWelcomeGif(gifResult: Result<File>): LogFoldable
    }
}

context(BotJoinedToGroupDeps)
fun handleBotJoinedToGroup() {
    events.launchEach {
        logBotJoined()
        val gif = readRandomGifToFile()
        val result = sendWelcomeGif(gif)
        result.fold()
    }
}
