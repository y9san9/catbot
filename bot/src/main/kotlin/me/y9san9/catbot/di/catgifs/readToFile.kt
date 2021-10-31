package me.y9san9.catbot.di.catgifs

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext
import java.io.File


@Suppress("BlockingMethodInNonBlockingContext")
suspend fun CatGifsProvider.readRandomGifToFile(): File = withContext(Dispatchers.IO) {
    val file = File.createTempFile(
        /*prefix = */"y9catbot-",
        /*suffix = */".gif"
    )

    randomGif()
        .collect {
            file.appendBytes(it)
        }

    return@withContext file
}
