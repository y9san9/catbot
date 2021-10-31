package me.y9san9.catbot.di.storage

import me.y9san9.catbot.di.requests.models.ChatMember

interface Storage {
    suspend fun init() {}

    /**
     * Spec: this method does not check for user in storage, just inserts is,
     * so [isUserJoinedFirstTime] should be called before it
     */
    suspend fun saveUserJoined(user: ChatMember)

    suspend fun isUserJoinedFirstTime(user: ChatMember): Boolean
}
