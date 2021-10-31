package me.y9san9.catbot.di.log

import me.y9san9.catgifs.di.log.Logger
import me.y9san9.catgifs.di.log.LogEvent

abstract class GifsStringLogger : Logger {
    final override fun processEvent(event: LogEvent) {
        val string = when (event) {
            is LogEvent.CachedFileUsed -> "Cached file used ${event.file.name}"
            is LogEvent.GifCached -> "New gif cached ${event.file.name}"
        }
        processEvent(event, string)
    }

    abstract fun processEvent(event: LogEvent, stringRepresentation: String)
}

object GifsPrintLogger : GifsStringLogger() {
    override fun processEvent(event: LogEvent, stringRepresentation: String) =
        PrintLogger.log(stringRepresentation)
}
