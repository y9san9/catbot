package me.y9san9.catbot.extension.bot

import dev.inmo.tgbotapi.bot.TelegramBot
import dev.inmo.tgbotapi.extensions.api.send.media.sendAnimation
import dev.inmo.tgbotapi.requests.abstracts.MultipartFile
import dev.inmo.tgbotapi.types.MessageEntity.textsources.TextSourcesList
import dev.inmo.tgbotapi.types.MessageIdentifier
import dev.inmo.tgbotapi.types.chat.abstracts.Chat
import dev.inmo.tgbotapi.utils.StorageFile
import me.y9san9.catbot.TelegramCatBotDeps
import java.io.File

context(TelegramCatBotDeps)
suspend fun TelegramBot.sendAnimationCached(
    chat: Chat,
    animation: File,
    entities: TextSourcesList = listOf(),
    replyToMessageId: MessageIdentifier? = null
) {
    val fileId = storage.getFileId(animation) ?: return sendAnimation(
        chat = chat,
        animation = MultipartFile(StorageFile(animation)),
        entities = entities,
        replyToMessageId = replyToMessageId
    ).let { message ->
        storage.putFileId(animation, message.content.media.fileId)
    }

    sendAnimation(
        chat = chat,
        animation = fileId,
        entities = entities,
        replyToMessageId = replyToMessageId
    )
}
