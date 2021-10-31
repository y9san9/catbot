package me.y9san9.catbot.di.log

import dev.inmo.tgbotapi.bot.TelegramBot
import dev.inmo.tgbotapi.extensions.api.send.sendMessage
import dev.inmo.tgbotapi.types.ChatId
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class TelegramLogger(
    private val chatId: Long,
    private val bot: TelegramBot,
    private val scope: CoroutineScope
) : LogWriter {
    private val messages = MutableSharedFlow<String>(
        extraBufferCapacity = 50,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    init {
        messages
            .onEach { message -> bot.sendMessage(ChatId(chatId), message) }
            .launchIn(scope)
    }

    override fun log(message: String) {
        scope.launch {
            messages.emit(message)
        }
    }
}
