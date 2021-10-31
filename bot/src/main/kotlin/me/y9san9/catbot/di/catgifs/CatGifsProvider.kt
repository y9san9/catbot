package me.y9san9.catbot.di.catgifs

import java.io.File

interface CatGifsProvider {
    suspend fun readRandomGifToFile(): File
}
