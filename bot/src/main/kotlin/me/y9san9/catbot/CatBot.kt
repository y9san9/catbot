package me.y9san9.catbot

import kotlinx.coroutines.launch
import me.y9san9.catbot.feature.botJoinedToGroup.handleBotJoinedToGroup
import me.y9san9.catbot.feature.inlineRequests.handleInlineRequests
import me.y9san9.catbot.feature.newUserJoined.handleNewUserJoined
import me.y9san9.catbot.feature.nyanCommand.handleNyanCommands
import me.y9san9.catbot.feature.startCommand.handleStartCommand

object CatBot {
    context(CatBotDeps)
    fun startNewInstance() = scope.launch {
        logger.logBotStarted()
        storage.init()

        with(botJoinedToGroupDeps) {
            handleBotJoinedToGroup()
        }
        with(newUserJoinedDeps) {
            handleNewUserJoined()
        }
        with(startCommandDeps) {
            handleStartCommand()
        }
        with(nyanCommandDeps) {
            handleNyanCommands()
        }
        // not all platforms may have this mode
        inlineRequestsDeps?.run {
            handleInlineRequests()
        }

        logger.logBotSetupFinished()
    }
}
