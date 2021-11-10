package me.y9san9.catgifs.cached.di.log

import java.io.File

sealed class LogEvent {
    class CachedFileUsed(val file: File, val thisLoopAvailable: Int, val filesCached: Int) : LogEvent()
    object RefreshingCache : LogEvent()
}
