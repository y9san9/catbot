package me.y9san9.catbot.feature.inlineRequests

import me.y9san9.catbot.feature.common.GifCommandDeps
import me.y9san9.catbot.feature.common.handleGifCommands

interface InlineRequestsDeps : GifCommandDeps

context(InlineRequestsDeps)
fun handleInlineRequests() = handleGifCommands()
