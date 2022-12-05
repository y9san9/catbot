package me.y9san9.catbot.di.log

import me.y9san9.catbot.log.LogEvent

fun interface Logger {
    fun processEvent(event: LogEvent)
}
