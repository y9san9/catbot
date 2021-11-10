package me.y9san9.catbot.di.log

import me.y9san9.catgifs.cached.di.log.Logger
import me.y9san9.catgifs.cached.di.log.LogEvent

class FromCacheGifsLogger(private val logAction: (LogEvent, String) -> Unit) : Logger {
    override fun processEvent(event: LogEvent) {
        val string = when (event) {
            is LogEvent.CachedFileUsed -> "From-cache gif ${event.file.name} used. Gifs in cache: ${event.thisLoopAvailable}/${event.filesCached}"
            is LogEvent.RefreshingCache -> "Cache refreshed because every gif was used"
        }
        logAction(event, string)
    }
}
