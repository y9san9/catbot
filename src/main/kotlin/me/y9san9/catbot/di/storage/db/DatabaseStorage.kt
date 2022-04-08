package me.y9san9.catbot.di.storage.db

import me.y9san9.catbot.di.requests.context.CatbotContext
import me.y9san9.catbot.di.requests.context.ChatContext
import me.y9san9.catbot.di.requests.context.ChatMemberContext
import me.y9san9.catbot.di.requests.context.UserContext
import me.y9san9.catbot.di.storage.Storage
import me.y9san9.catbot.handlers.ChatSettings

class DatabaseStorage(
    private val longsStorage: LongStorage
    // in future there may be more storages to select from
) : Storage {
    override suspend fun init(bot: CatbotContext) {
        when (bot) {
            is CatbotContext.WithLongChatId -> longsStorage.init(bot)
        }
    }

    override suspend fun saveUserJoined(user: ChatMemberContext) {
        when (user) {
            is ChatMemberContext.WithLongId -> longsStorage.saveUserJoined(user)
        }
    }

    override suspend fun isUserJoinedFirstTime(user: ChatMemberContext): Boolean {
        return when (user) {
            is ChatMemberContext.WithLongId -> longsStorage.isUserJoinedFirstTime(user)
        }
    }

    override suspend fun getChatSettings(chat: ChatContext): ChatSettings {
        return when (chat) {
            is ChatContext.WithLongId -> longsStorage.getChatSettings(chat)
        }
    }

    interface LongStorage {
        suspend fun init(bot: CatbotContext.WithLongChatId) {}
        suspend fun saveUserJoined(user: ChatMemberContext.WithLongId)
        suspend fun isUserJoinedFirstTime(user: ChatMemberContext.WithLongId): Boolean
        suspend fun getChatSettings(chat: ChatContext.WithLongId): ChatSettings
    }
}
