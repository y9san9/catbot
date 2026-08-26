package me.y9san9.catbot.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

interface CoroutineScopeDeps {
    val scope: CoroutineScope

    fun <T> Flow<T>.launchEach(handler: suspend T.() -> Unit) =
        onEach(handler)
            .launchIn(scope)
}
