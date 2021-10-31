package me.y9san9.catgifs

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import me.y9san9.catgifs.di.log.LogEvent
import me.y9san9.catgifs.di.log.Logger
import me.y9san9.catgifs.internal.RawApi
import me.y9san9.catgifs.internal.extensions.byteReadChannel.readAsFlow
import java.io.File

class CatGifsClient(
    private val logger: Logger = Logger {},
    private val minDownloadInterval: Long = 10_000
) {
    private suspend fun randomGif(bufferSize: Int = DEFAULT_BUFFER_SIZE) = RawApi
        .randomGifStream()
        .readAsFlow(bufferSize)

    private suspend fun readRandomGifToFile(directory: File): File {
        @Suppress("BlockingMethodInNonBlockingContext")
        val file = withContext(Dispatchers.IO) {
            File.createTempFile(
                /*prefix = */"y9catbot-",
                /*suffix = */".gif",
                /*dir = */directory
            )
        }
        withContext(Dispatchers.IO) {
            randomGif()
                .collect(file::appendBytes)
        }
        return file
    }

    // this is "global" variable for every flow that indicated when was the last gif
    // downloaded
    private var lastTimeDownloaded: Long = 0
    // used to make download process synchronous for each client
    private var mutex: Mutex = Mutex()

    private suspend fun FlowCollector<File>.readFiles(directory: File) {
        while (true) {
            mutex.withLock {
                if (System.currentTimeMillis() - lastTimeDownloaded > minDownloadInterval) {
                    val file = readRandomGifToFile(directory)
                    lastTimeDownloaded = System.currentTimeMillis()
                    logger.processEvent(LogEvent.GifCached(file))
                    emit(file)
                } else {
                    val cachedFiles = directory.listFiles() ?: error("Cannot list files")
                    val file = cachedFiles.random()
                    logger.processEvent(LogEvent.CachedFileUsed(file))
                    emit(file)
                }
            }
        }
    }

    fun randomGifFiles(
        directory: File = File(System.getProperty("user.dir"), "catgifs-tmp-dir"),
        bufferSize: Int = 20 // how much gifs the bot should pre-download
    ): Flow<File> {
        if (!directory.exists())
            directory.mkdirs()

        require(directory.isDirectory) { "I can only store gifs to directories, but not in files, which $directory is" }

        return flow {
            readFiles(directory)
        }.buffer(capacity = bufferSize)
    }
}
