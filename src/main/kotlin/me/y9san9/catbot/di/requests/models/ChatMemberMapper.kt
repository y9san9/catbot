package me.y9san9.catbot.di.requests.models

import dev.inmo.tgbotapi.types.chat.abstracts.PublicChat as InMoChat
import dev.inmo.tgbotapi.types.User as InMoUser

fun ChatMember(user: InMoUser, chat: InMoChat): ChatMember = ChatMember(
    user = user.asUser,
    chat = chat.asChat
)
