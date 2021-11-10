package me.y9san9.catgifs.cataas.di.log

fun interface Logger {
    fun processEvent(event: LogEvent)
}
