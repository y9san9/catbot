package me.y9san9.catbot.di.log

import dev.inmo.tgbotapi.bot.TelegramBot
import dev.inmo.tgbotapi.extensions.api.send.sendMessage
import dev.inmo.tgbotapi.types.ChatId
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class TelegramLogger(
    private val chatId: Long,
    private val bot: TelegramBot,
    private val scope: CoroutineScope,
    messagesBufferSize: Int = 50,
    private val logMessageDelay: Long = 3_000
) : LogWriter {
    private val messages = MutableSharedFlow<String>(
        extraBufferCapacity = messagesBufferSize,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    init {
        messages
            .onEach { message ->
                bot.sendMessage(ChatId(chatId), message)
                delay(logMessageDelay)
            }
            .launchIn(scope)
    }

    override fun log(message: String) {
        scope.launch {
            messages.emit(message)
        }
    }
}
