package me.y9san9.catbot.di.requests.context

import me.y9san9.catbot.di.requests.models.TextEntity

interface UserContext : ChatContext {
    val mention: TextEntity.Mention

    sealed interface WithLongId : UserContext, ChatContext.WithLongId

    interface Telegram : WithLongId, ChatContext.Telegram
}
