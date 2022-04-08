package me.y9san9.catbot.handlers

import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import me.y9san9.catbot.CatBotDependencies

class ChatSettings(val languageCode: String, val welcomeMessage: String)

fun handleBotSettings(dependencies: CatBotDependencies) =
    with(dependencies) {
        executor.groupSettingsCommands.onEach { message ->
            val settings = storage.getChatSettings(message.chat)
        }.launchIn(scope)
    }
