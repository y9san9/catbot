package me.y9san9.catbot.di.requests.context

import dev.inmo.tgbotapi.bot.TelegramBot
import dev.inmo.tgbotapi.extensions.api.send.media.sendAnimation
import dev.inmo.tgbotapi.extensions.utils.formatting.link
import dev.inmo.tgbotapi.requests.abstracts.MultipartFile
import dev.inmo.tgbotapi.types.ChatId
import dev.inmo.tgbotapi.types.abstracts.WithOptionalLanguageCode
import dev.inmo.tgbotapi.types.chat.abstracts.Chat
import dev.inmo.tgbotapi.types.chat.abstracts.PrivateChat
import dev.inmo.tgbotapi.types.chat.abstracts.PublicChat
import dev.inmo.tgbotapi.types.chat.abstracts.UsernameChat
import dev.inmo.tgbotapi.utils.StorageFile
import me.y9san9.catbot.di.requests.context.ChatContext
import me.y9san9.catbot.di.requests.models.TextEntities
import me.y9san9.catbot.di.requests.models.asInMoTextEntities
import java.io.File

class TelegramChatContext(
    private val bot: TelegramBot,
    private val chat: Chat,
    private val fileIdStorage: TelegramContext.FileIdStorage
) : ChatContext.Telegram {

    override val id: Long get() = chat.id.chatId
    override val title: String get() = chat.title
    override val link: String? get() = chat.link
    override val languageCode: String? get() = (chat as? WithOptionalLanguageCode)?.languageCode

    override suspend fun sendGif(entities: TextEntities, gif: File): Boolean = runCatching {
        when(val fileId = fileIdStorage.getFileId(gif)) {
            null -> bot.sendAnimation(
                chatId = ChatId(id),
                animation = MultipartFile(
                    file = StorageFile(gif)
                ),
                entities = entities.asInMoTextEntities
            )
            else -> bot.sendAnimation(
                chatId = ChatId(id),
                animation = fileId,
                entities = entities.asInMoTextEntities
            )
        }
    }.isSuccess
}

private val Chat.title get() = when (this) {
    is PrivateChat -> "$firstName $lastName"
    is PublicChat -> title
    else -> error("I don't know how to name this chat, oops ( $this )")
}
