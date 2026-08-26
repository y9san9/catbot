package me.y9san9.catbot.feature.inlineRequests

import dev.inmo.tgbotapi.bot.TelegramBot
import dev.inmo.tgbotapi.extensions.api.send.media.sendAnimation
import dev.inmo.tgbotapi.requests.abstracts.FileId
import dev.inmo.tgbotapi.requests.abstracts.MultipartFile
import dev.inmo.tgbotapi.requests.abstracts.asMultipartFile
import dev.inmo.tgbotapi.types.ChatId
import me.y9san9.catbot.Storage
import me.y9san9.catbot.di.catgifs.CatGifsProvider

class CatGifFileIdRepository(
    private val bot: TelegramBot,
    private val storage: Storage,
    private val gifsProvider: CatGifsProvider,
    private val cachedGifsChatId: ChatId
) {
    suspend fun getRandomFileId(): Result<FileId> = runCatching {
        val file = gifsProvider.readRandomGifToFile().getOrThrow()
        return@runCatching storage.getFileId(file)
            ?: bot.sendAnimation(
                chatId = cachedGifsChatId,
                animation = file.asMultipartFile(),
                text = "THIS GIF WAS SENT TO RETRIEVE FILE ID FOR INLINE REQUEST!"
            ).content.media.fileId.also { fileId -> storage.putFileId(file, fileId) }
    }
}
