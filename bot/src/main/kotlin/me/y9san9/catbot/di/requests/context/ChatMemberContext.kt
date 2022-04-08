package me.y9san9.catbot.di.requests.context

sealed interface ChatMemberContext : UserContext {
    val chat: ChatContext

    sealed interface WithLongId : ChatMemberContext, UserContext.WithLongId {
        override val chat: ChatContext.WithLongId
    }

    interface Telegram : WithLongId, UserContext.Telegram {
        override val chat: ChatContext.Telegram
    }
}
