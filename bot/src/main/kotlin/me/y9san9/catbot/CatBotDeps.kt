package me.y9san9.catbot

import kotlinx.coroutines.CoroutineScope
import me.y9san9.catbot.feature.botJoinedToGroup.BotJoinedToGroupDeps
import me.y9san9.catbot.feature.inlineRequests.InlineRequestsDeps
import me.y9san9.catbot.feature.newUserJoined.NewUserJoinedDeps
import me.y9san9.catbot.feature.nyanCommand.NyanCommandDeps
import me.y9san9.catbot.feature.startCommand.StartCommandDeps

class CatBotDeps(
    val botJoinedToGroupDeps: BotJoinedToGroupDeps,
    val newUserJoinedDeps: NewUserJoinedDeps,
    val startCommandDeps: StartCommandDeps,
    val nyanCommandDeps: NyanCommandDeps,
    val inlineRequestsDeps: InlineRequestsDeps?,
    val storage: Storage,
    val logger: Logger,
    val scope: CoroutineScope
) {
    interface Logger {
        fun logBotStarted()
        fun logBotSetupFinished()
    }

    interface Storage {
        suspend fun init()
    }
}
