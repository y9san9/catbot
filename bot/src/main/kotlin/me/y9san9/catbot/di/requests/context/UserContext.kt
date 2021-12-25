package me.y9san9.catbot.di.requests.context

interface UserContext : ChatContext {
    interface Telegram : UserContext, ChatContext.Telegram
}
