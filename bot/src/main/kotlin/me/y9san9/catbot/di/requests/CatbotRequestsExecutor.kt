package me.y9san9.catbot.di.requests

import kotlinx.coroutines.flow.Flow
import me.y9san9.catbot.di.requests.models.Chat
import me.y9san9.catbot.di.requests.models.ChatMember
import me.y9san9.catbot.di.requests.models.TextEntities

import java.io.File

interface CatbotRequestsExecutor {
    /**
     * Spec: users added by admins should be ignored (so bots will be automatically ignored too)
     */
    val newMembersJoined: Flow<ChatMember>
    val botJoinedToGroup: Flow<Chat>

    val startCommands: Flow<Chat>

    suspend fun sendGif(chatId: Long, text: TextEntities, gif: File)
}
