package me.y9san9.catbot.di.requests.context

import dev.inmo.tgbotapi.bot.TelegramBot
import dev.inmo.tgbotapi.extensions.api.send.media.sendAnimation
import dev.inmo.tgbotapi.extensions.api.send.replyWithAnimation
import dev.inmo.tgbotapi.requests.abstracts.MultipartFile
import dev.inmo.tgbotapi.types.ChatId
import dev.inmo.tgbotapi.types.message.abstracts.Message
import dev.inmo.tgbotapi.types.message.abstracts.PossiblyReplyMessage
import dev.inmo.tgbotapi.utils.StorageFile
import me.y9san9.catbot.di.requests.models.TextEntities
import me.y9san9.catbot.di.requests.models.asInMoTextEntities
import java.io.File

class TelegramMessageContext(
    private val bot: TelegramBot,
    private val message: Message,
    private val fileIdStorage: TelegramContext.FileIdStorage
) : MessageContext.Telegram {
    override val chat: ChatContext.Telegram get() = TelegramChatContext(bot, message.chat,fileIdStorage)
    override val replyMessage: MessageContext.Telegram? get() =
        (message as? PossiblyReplyMessage)?.replyTo?.let { TelegramMessageContext(bot, message, fileIdStorage) }

    override suspend fun replyWithGif(entities: TextEntities, gif: File): Boolean =
        runCatching {
            when(val fileId = fileIdStorage.getFileId(gif)) {
                null -> bot.replyWithAnimation(
                    to = message,
                    animation = MultipartFile(
                        file = StorageFile(gif)
                    ),
                    entities = entities.asInMoTextEntities
                )
                else -> bot.replyWithAnimation(
                    to = message,
                    animation = fileId,
                    entities = entities.asInMoTextEntities
                )
            }
        }.isSuccess
}
