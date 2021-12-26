package me.y9san9.catbot.di.log

import me.y9san9.catgifs.cataas.di.log.Logger
import me.y9san9.catgifs.cataas.di.log.LogEvent

class CataasGifsLogger(private val logAction: (LogEvent, String) -> Unit) : Logger {
    override fun processEvent(event: LogEvent) {
        val string = when (event) {
            is LogEvent.CachedFileUsed -> "Cached file used ${event.file.name}. Cached amount: ${event.cachedAmount}"
            is LogEvent.GifCached -> "New gif cached ${event.file.name}. Cached amount now: ${event.cachedAmount}"
            is LogEvent.GifDeletedDueToCacheOverflow -> "Gif deleted ${event.file.name} due to cache overflow. Cached amount now: ${event.cachedAmount}"
        }
        logAction(event, string)
    }
}
