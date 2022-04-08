package me.y9san9.catbot.di.storage

import dev.inmo.tgbotapi.requests.abstracts.FileId
import me.y9san9.catbot.di.requests.context.TelegramContext
import me.y9san9.catbot.utils.getMD5ForSmallData
import java.io.File

object MemoryFileIdStorage : TelegramContext.FileIdStorage {
    private val files: MutableMap<String, FileId> = mutableMapOf()

    override fun getFileId(file: File): FileId? = files[file.getMD5ForSmallData()]

    override fun putFileId(file: File, id: FileId) {
        files[file.getMD5ForSmallData()] = id
    }
}
