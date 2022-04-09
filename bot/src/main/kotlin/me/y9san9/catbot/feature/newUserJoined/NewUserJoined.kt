package me.y9san9.catbot.feature.newUserJoined

import kotlinx.coroutines.flow.Flow
import me.y9san9.catbot.di.CoroutineScopeDeps
import me.y9san9.catbot.di.catgifs.CatGifsProvider
import me.y9san9.catbot.feature.common.LogFoldable
import me.y9san9.catbot.feature.common.fold
import java.io.File

interface NewUserJoinedDeps : CoroutineScopeDeps, CatGifsProvider {
    val events: Flow<Event>

    interface Event {
        suspend fun isUserJoinedFirstTime(): Boolean

        fun logNewMemberJoined()
        suspend fun saveUserJoined()
        fun logUserSaved()
        suspend fun sendNewMemberCaptchaMessage(gifResult: Result<File>): LogFoldable

        fun logOldMemberJoined()
        suspend fun sendOldMemberCaptchaMessage(gifResult: Result<File>): LogFoldable
    }
}

context(NewUserJoinedDeps)
fun handleNewUserJoined() {
    events.launchEach {
        val gif = readRandomGifToFile()
        val result = when (isUserJoinedFirstTime()) {
            true -> {
                logNewMemberJoined()
                saveUserJoined()
                logUserSaved()
                sendNewMemberCaptchaMessage(gif)
            }
            false -> {
                logOldMemberJoined()
                sendOldMemberCaptchaMessage(gif)
            }
        }
        result.fold()
    }
}
