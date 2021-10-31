package me.y9san9.catgifs.di.log

import java.io.File

sealed class LogEvent {
    class CachedFileUsed(val file: File) : LogEvent()
    class GifCached(val file: File) : LogEvent()
}
