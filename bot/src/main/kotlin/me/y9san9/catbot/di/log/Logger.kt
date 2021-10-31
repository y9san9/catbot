package me.y9san9.catbot.di.log

fun interface Logger {
    fun processEvent(event: LogEvent)
}
