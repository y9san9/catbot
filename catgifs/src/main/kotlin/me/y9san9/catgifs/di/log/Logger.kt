package me.y9san9.catgifs.di.log

fun interface Logger {
    fun processEvent(event: LogEvent)
}
