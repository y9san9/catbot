package me.y9san9.catbot.di.requests.models

import dev.inmo.tgbotapi.types.abstracts.WithOptionalLanguageCode
import dev.inmo.tgbotapi.types.chat.abstracts.PrivateChat

val PrivateChat.asUser get() = User(
    id = id.chatId,
    languageCode = (this as? WithOptionalLanguageCode)?.languageCode,
    firstName = firstName,
    lastName = lastName,
    username = username?.username
)
