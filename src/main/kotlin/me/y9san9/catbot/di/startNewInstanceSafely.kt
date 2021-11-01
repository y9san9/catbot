package me.y9san9.catbot.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import me.y9san9.catbot.di.log.LogWriter

fun startNewInstanceSafely(
    scope: CoroutineScope,
    logWriter: LogWriter,
    botStarter: (CoroutineScope) -> Unit,
    crashRecovery: (Throwable) -> Unit = { startNewInstanceSafely(scope, logWriter, botStarter) }
) = scope.launch {
    try {
        coroutineScope {
            botStarter(this)
        }
    } catch (t: Throwable) {
        logWriter.log(message = "Exception occurred: ${t.stackTraceToString()}")
        crashRecovery(t)
    }
}
