package me.y9san9.catgifs.cataas

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import me.y9san9.catgifs.cataas.di.log.LogEvent
import me.y9san9.catgifs.cataas.di.log.Logger
import me.y9san9.catgifs.cataas.internal.RawApi
import me.y9san9.catgifs.cataas.internal.extensions.byteReadChannel.readAsFlow
import java.io.File

class CatGifsClient(
    private val logger: Logger = Logger {},
    private val minDownloadInterval: Long = 10_000,
    private val maxGifsCached: Int = 500,
    private val directory: File = File(System.getProperty("user.dir"), "catgifs-tmp-dir"),
    private val bufferSize: Int = 20 // how much gifs the bot should pre-download
) {
    init {
        require(maxGifsCached > 0) { "At least one cache file required for catgifs proper work" }
    }

    private suspend fun randomGif(bufferSize: Int = DEFAULT_BUFFER_SIZE) = RawApi
        .randomGifStream()
        .readAsFlow(bufferSize)

    private suspend fun readRandomGifToFile(directory: File): Result<File> {
        val file = runCatching {
            @Suppress("BlockingMethodInNonBlockingContext")
            val file = withContext(Dispatchers.IO) {
                File.createTempFile(
                    /* prefix = */"y9catbot-",
                    /* suffix = */".gif",
                    /* directory = */directory
                )
            }
            withContext(Dispatchers.IO) {
                randomGif()
                    .collect(file::appendBytes)
            }
            return@runCatching file
        }
        return file
    }

    // this is "global" variable for every flow that indicated when was the last gif
    // downloaded
    private var lastTimeDownloaded: Long = 0
    // used to make download process synchronous for each client
    private var mutex: Mutex = Mutex()

    private suspend fun FlowCollector<Result<File>>.readFiles(directory: File) {
        while (true) {
            val cachedFiles = directory.listFiles() ?: error("Cannot list files")
            if (cachedFiles.size >= maxGifsCached) {
                val file = cachedFiles.first()
                file.delete()
                logger.processEvent(LogEvent.GifDeletedDueToCacheOverflow(file, cachedAmount = cachedFiles.size - 1))
            }

            mutex.withLock {
                if (System.currentTimeMillis() - lastTimeDownloaded > minDownloadInterval) {
                    val file = readRandomGifToFile(directory)
                    lastTimeDownloaded = System.currentTimeMillis()
                    if (file.isSuccess)
                        logger.processEvent(
                            LogEvent.GifCached(file.getOrThrow(), cachedAmount = cachedFiles.size + 1)
                        )
                    emit(file)
                } else {
                    val file = cachedFiles.random()
                    logger.processEvent(LogEvent.CachedFileUsed(file, cachedAmount = cachedFiles.size))
                    emit(Result.success(file))
                }
            }
        }
    }

    val randomGifFiles: Flow<Result<File>> get() {
        if (!directory.exists())
            directory.mkdirs()

        require(directory.isDirectory) { "I can only store gifs to directories, but not in files, which $directory is" }

        return flow {
            readFiles(directory)
        }.buffer(capacity = bufferSize)
    }
}
