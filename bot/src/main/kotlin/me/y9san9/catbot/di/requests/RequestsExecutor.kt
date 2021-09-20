package me.y9san9.catbot.di.requests

import kotlinx.coroutines.flow.Flow
import me.y9san9.catbot.di.requests.text.TextEntities
import me.y9san9.catbot.di.requests.user.ChatMember
import java.io.File

interface RequestsExecutor {
    /**
     * If the member was added by someone, update should be skipped
     */
    val newMembersSelfJoined: Flow<ChatMember>

    suspend fun sendGif(chatId: Long, text: TextEntities, gif: File)
}
