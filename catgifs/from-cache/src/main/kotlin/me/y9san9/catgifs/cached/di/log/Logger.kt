package me.y9san9.catgifs.cached.di.log

fun interface Logger {
    fun processEvent(event: LogEvent)
}
