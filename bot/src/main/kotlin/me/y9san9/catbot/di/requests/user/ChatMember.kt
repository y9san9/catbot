package me.y9san9.catbot.di.requests.user

fun ChatMember(id: Long, firstName: String, lastName: String, languageCode: String?, chatId: Long): ChatMember =
    ChatMemberModel(id, firstName, lastName, languageCode, chatId)

interface ChatMember : User {
    val chatId: Long
}

data class ChatMemberModel(
    override val id: Long,
    override val firstName: String,
    override val lastName: String,
    override val languageCode: String?,
    override val chatId: Long
) : ChatMember
