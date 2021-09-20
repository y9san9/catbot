package me.y9san9.catbot.di.requests.user

fun ChatMember(id: Long, firstName: String, lastName: String, chatId: Long): ChatMember =
    ChatMemberModel(id, firstName, lastName, chatId)

interface ChatMember : User {
    val chatId: Long
}

data class ChatMemberModel(
    override val id: Long,
    override val firstName: String,
    override val lastName: String,
    override val chatId: Long
) : ChatMember
