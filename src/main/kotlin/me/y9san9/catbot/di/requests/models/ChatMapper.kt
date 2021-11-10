package me.y9san9.catbot.di.requests.models

import dev.inmo.tgbotapi.types.chat.abstracts.PrivateChat
import dev.inmo.tgbotapi.types.chat.abstracts.Chat as InMoChat
import dev.inmo.tgbotapi.types.chat.abstracts.PublicChat
import dev.inmo.tgbotapi.types.chat.abstracts.UsernameChat

val InMoChat.asChat get() = when(this) {
    is PublicChat -> Chat(
        id = id.chatId,
        title = title,
        username = (this as? UsernameChat)?.username?.username?.let(::Username)
    )
    is PrivateChat -> asUser
    else -> error("Unsupported type of chat: $this")
}
