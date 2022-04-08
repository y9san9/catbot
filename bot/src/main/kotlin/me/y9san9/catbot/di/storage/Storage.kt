package me.y9san9.catbot.di.storage

import me.y9san9.catbot.di.requests.context.CatbotContext
import me.y9san9.catbot.di.requests.context.ChatContext
import me.y9san9.catbot.di.requests.context.ChatMemberContext
import me.y9san9.catbot.handlers.ChatSettings

interface Storage {
    suspend fun init(bot: CatbotContext) {}

    /**
     * Spec: this method does not check for user in storage, just inserts is,
     * so [isUserJoinedFirstTime] should be called before it
     */
    suspend fun saveUserJoined(user: ChatMemberContext)

    suspend fun isUserJoinedFirstTime(user: ChatMemberContext): Boolean

    suspend fun getChatSettings(chat: ChatContext): ChatSettings
}
