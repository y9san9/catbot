package me.y9san9.catbot.di.requests.models

interface ChatMember : User {
    val chat: Chat
}

fun ChatMember(user: User, chat: Chat): ChatMember =
    ChatMember(user.id, user.firstName, user.lastName, user.languageCode, user.username, chat)

fun ChatMember(id: Long, firstName: String, lastName: String, languageCode: String?, username: String?, chat: Chat): ChatMember =
    _ChatMember(id, firstName, lastName, languageCode, username, chat)

@Suppress("ClassName")
private data class _ChatMember(
    override val id: Long,
    override val firstName: String,
    override val lastName: String,
    override val languageCode: String?,
    override val username: String?,
    override val chat: Chat
) : ChatMember
