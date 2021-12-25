package me.y9san9.catbot.di.requests.context

sealed interface ChatMemberContext : UserContext {
    /**
     * Spec: this method does not check for the member in storage, just inserts is,
     * so [isUserJoinedFirstTime] should be called before it
     */
    suspend fun saveJoined()
    suspend fun isJoinedFirstTime(): Boolean

    val chat: ChatContext

    interface Telegram : ChatMemberContext, UserContext.Telegram
}
