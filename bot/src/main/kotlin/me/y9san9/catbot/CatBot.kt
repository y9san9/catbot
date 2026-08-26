package me.y9san9.catbot

import kotlinx.coroutines.launch
import me.y9san9.catbot.feature.botJoinedToGroup.handleBotJoinedToGroup
import me.y9san9.catbot.feature.inlineRequests.handleInlineRequests
import me.y9san9.catbot.feature.newUserJoined.handleNewUserJoined
import me.y9san9.catbot.feature.nyanCommand.handleNyanCommands
import me.y9san9.catbot.feature.startCommand.handleStartCommand

object CatBot {
    context(deps: CatBotDeps)
    fun startNewInstance() = deps.scope.launch {
        deps.logger.logBotStarted()
        deps.storage.init()

        context(deps.botJoinedToGroupDeps) {
            handleBotJoinedToGroup()
        }
        context(deps.newUserJoinedDeps) {
            handleNewUserJoined()
        }
        context(deps.startCommandDeps) {
            handleStartCommand()
        }
        context(deps.nyanCommandDeps) {
            handleNyanCommands()
        }
        // not all platforms may have this mode
        val inlineRequestsDeps = deps.inlineRequestsDeps
        if(inlineRequestsDeps != null) {
            context(inlineRequestsDeps) {
                handleInlineRequests()
            }
        }

        deps.logger.logBotSetupFinished()
    }
}
