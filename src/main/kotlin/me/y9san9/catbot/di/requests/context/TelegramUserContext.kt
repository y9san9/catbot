package me.y9san9.catbot.di.requests.context

import dev.inmo.tgbotapi.bot.TelegramBot
import dev.inmo.tgbotapi.types.User
import me.y9san9.catbot.di.requests.models.TextEntity

class TelegramUserContext(
    private val bot: TelegramBot,
    private val user: User,
    fileIdStorage: TelegramContext.FileIdStorage
) : UserContext.Telegram, ChatContext.Telegram by TelegramChatContext(bot, user, fileIdStorage) {
    override val mention: TextEntity.Mention get() = TextEntity.Mention(title, user = this)
}
