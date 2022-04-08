package me.y9san9.catbot.di.requests.context

import dev.inmo.micro_utils.common.filesize
import dev.inmo.tgbotapi.bot.TelegramBot
import dev.inmo.tgbotapi.extensions.api.answers.answerInlineQuery
import dev.inmo.tgbotapi.extensions.api.send.media.sendAnimation
import dev.inmo.tgbotapi.requests.abstracts.MultipartFile
import dev.inmo.tgbotapi.types.ChatId
import dev.inmo.tgbotapi.types.InlineQueries.InlineQueryResult.InlineQueryResultAudioCachedImpl
import dev.inmo.tgbotapi.types.InlineQueries.InlineQueryResult.InlineQueryResultGifCachedImpl
import dev.inmo.tgbotapi.types.InlineQueries.InlineQueryResult.InlineQueryResultGifImpl
import dev.inmo.tgbotapi.types.InlineQueries.InlineQueryResult.abstracts.results.gif.InlineQueryResultGif
import dev.inmo.tgbotapi.types.InlineQueries.query.InlineQuery
import dev.inmo.tgbotapi.types.InlineQueryIdentifier
import dev.inmo.tgbotapi.utils.StorageFile
import java.io.File

class TelegramInlineRequestContext(
    private val bot: TelegramBot,
    private val inlineQuery: InlineQuery,
    private val fileIdStorage: TelegramContext.FileIdStorage,
    private val cachedGifsChatId: Long
) : InlineRequestContext.Telegram {
    override val from: UserContext get() =
        TelegramUserContext(bot, inlineQuery.from, fileIdStorage)

    override suspend fun answerWithGif(gif: File): Boolean = runCatching {
        val fileId = fileIdStorage.getFileId(gif) ?: bot.sendAnimation(
            chatId = ChatId(cachedGifsChatId),
            animation = MultipartFile(
                file = StorageFile(
                    file = gif
                )
            ),
            text = "THIS GIF WAS SENT TO RETRIEVE FILE ID FOR INLINE REQUEST!"
        ).content.media.fileId
        bot.answerInlineQuery(
            inlineQuery = inlineQuery,
            results = listOf(
                InlineQueryResultGifCachedImpl(
                    id = "0",
                    fileId = fileId
                )
            ),
            cachedTime = 0
        )
    }.isSuccess

    override suspend fun answer(): Boolean = runCatching {
        bot.answerInlineQuery(inlineQuery, cachedTime = 0)
    }.isSuccess
}
