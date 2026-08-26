package me.y9san9.catbot.extension.bot

import dev.inmo.tgbotapi.bot.TelegramBot
import dev.inmo.tgbotapi.extensions.api.send.media.sendAnimation
import dev.inmo.tgbotapi.requests.abstracts.MultipartFile
import dev.inmo.tgbotapi.requests.abstracts.asMultipartFile
import dev.inmo.tgbotapi.types.MessageIdentifier
import dev.inmo.tgbotapi.types.ReplyParameters
import dev.inmo.tgbotapi.types.chat.Chat
import dev.inmo.tgbotapi.types.message.textsources.TextSourcesList
import me.y9san9.catbot.TelegramCatBotDeps
import java.io.File

context(deps: TelegramCatBotDeps)
suspend fun TelegramBot.sendAnimationCached(
    chat: Chat,
    animation: File,
    entities: TextSourcesList = listOf(),
    replyToMessageId: MessageIdentifier? = null
) {
    val fileId = deps.storage.getFileId(animation) ?: return sendAnimation(
        chat = chat,
        animation = animation.asMultipartFile(),
        entities = entities,
        replyParameters = replyToMessageId?.let {
            ReplyParameters(chatIdentifier = chat.id, messageId = it)
        }
    ).let { message ->
        deps.storage.putFileId(animation, message.content.media.fileId)
    }

    sendAnimation(
        chat = chat,
        animation = fileId,
        entities = entities,
        replyParameters = replyToMessageId?.let {
            ReplyParameters(chatIdentifier = chat.id, messageId = it)
        }
    )
}
