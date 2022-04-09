package me.y9san9.catbot.feature.startCommand

import me.y9san9.catbot.feature.common.GifCommandDeps
import me.y9san9.catbot.feature.common.handleGifCommands

interface StartCommandDeps : GifCommandDeps

context(StartCommandDeps)
fun handleStartCommand() = handleGifCommands()
