package me.y9san9.catgifs.cataas.di.log

import java.io.File

sealed class LogEvent {
    class GifDeletedDueToCacheOverflow(val file: File, val cachedAmount: Int) : LogEvent()
    class CachedFileUsed(val file: File, val cachedAmount: Int) : LogEvent()
    class GifCached(val file: File, val cachedAmount: Int) : LogEvent()
}
