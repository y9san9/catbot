package me.y9san9.catbot.feature.nyanCommand

import me.y9san9.catbot.feature.common.GifCommandDeps
import me.y9san9.catbot.feature.common.handleGifCommands

interface NyanCommandDeps : GifCommandDeps

context(NyanCommandDeps)
fun handleNyanCommands() = handleGifCommands()
