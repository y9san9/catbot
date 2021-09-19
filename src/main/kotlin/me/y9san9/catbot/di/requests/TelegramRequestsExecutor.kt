package me.y9san9.catbot.di.requests

import dev.inmo.tgbotapi.bot.Ktor.telegramBot
import dev.inmo.tgbotapi.extensions.api.send.media.sendAnimation
import dev.inmo.tgbotapi.requests.abstracts.MultipartFile
import dev.inmo.tgbotapi.types.ChatId
import dev.inmo.tgbotapi.utils.StorageFile
import java.io.File

class TelegramRequestsExecutor(botToken: String) : RequestsExecutor {
    private val bot = telegramBot(botToken)

    override suspend fun sendGif(chatId: Long, gif: File) = bot.sendAnimation(
        chatId = ChatId(chatId),
        animation = MultipartFile(file = StorageFile(gif)),
    ).let {}
}
