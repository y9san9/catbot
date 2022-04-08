package me.y9san9.catbot.di.requests.context

import dev.inmo.tgbotapi.bot.TelegramBot
import dev.inmo.tgbotapi.types.User
import dev.inmo.tgbotapi.types.chat.abstracts.Chat
import java.io.File

class TelegramChatMemberContext(
    private val bot: TelegramBot,
    private val user: User,
    private val _chat: Chat,
    private val fileIdStorage: TelegramContext.FileIdStorage
) : ChatMemberContext.Telegram, UserContext.Telegram by TelegramUserContext(bot, user, fileIdStorage) {
    override suspend fun sendGif(text: String, gif: File): Boolean =
        super<ChatMemberContext.Telegram>.sendGif(text, gif)
    override val chat: ChatContext.Telegram get() = TelegramChatContext(bot, _chat, fileIdStorage)
}
