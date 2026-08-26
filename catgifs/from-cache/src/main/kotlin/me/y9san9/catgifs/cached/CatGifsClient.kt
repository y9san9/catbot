package me.y9san9.catgifs.cached

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import me.y9san9.catgifs.cached.di.log.LogEvent
import me.y9san9.catgifs.cached.di.log.Logger
import java.io.File

class CatGifsClient(
    private val logger: Logger = Logger { },
    private val directory: File = File(System.getProperty("user.dir"), "catgifs-tmp-dir")
) {
    val randomGifFiles: Flow<File>
        get() {
            if (!directory.exists())
                directory.mkdirs()

            require(directory.isDirectory) { "I can only store gifs to directories, but not in files, which $directory is" }

            // This works in a such way:
            // First, it lists the directory and shuffles files inside
            // Then file returns one by one file and when there is no files left,
            // It lists and shuffles directory again and so on
            return generateSequence { directory.listFiles()?.takeIf { it.isNotEmpty() } }
                .onEach { logger.processEvent(LogEvent.RefreshingCache) }
                .flatMap { directoryFiles ->
                    directoryFiles.apply { shuffle() }
                        .asSequence()
                        .onEachIndexed { i, file ->
                            logger.processEvent(LogEvent.CachedFileUsed(
                                file, thisLoopAvailable = directoryFiles.size - i - 1,
                                filesCached = directoryFiles.size
                            ))
                        }
                }
                .asFlow()
        }
}
