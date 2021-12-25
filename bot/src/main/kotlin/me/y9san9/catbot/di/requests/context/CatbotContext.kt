package me.y9san9.catbot.di.requests.context

import kotlinx.coroutines.flow.Flow

// Marked with sealed, so there will be the possibility to extend only platform interfaces like
// TelegramRequestsExecutor, etc.
sealed interface CatbotContext {
    /**
     * Spec: users added by admins should be ignored (so bots will be automatically ignored too)
     */
    val newMembersJoined: Flow<ChatMemberContext>
    val botJoinedToGroup: Flow<ChatContext>

    val startCommands: Flow<MessageContext>

    sealed interface HasInlineRequests : CatbotContext {
        val inlineRequests: Flow<InlineRequestContext>
    }

    interface Telegram : HasInlineRequests {
        override val newMembersJoined: Flow<ChatMemberContext.Telegram>
        override val botJoinedToGroup: Flow<ChatContext.Telegram>
        override val startCommands: Flow<MessageContext.Telegram>
        override val inlineRequests: Flow<InlineRequestContext.Telegram>
    }
}
