package me.y9san9.catbot.di.requests.models

interface Chat : WithOptionalUsername {
    val id: Long
    val title: String
    override val username: String?
}

fun Chat(
    id: Long,
    title: String,
    username: String?
): Chat = _Chat(id, title, username)

@Suppress("ClassName")
private data class _Chat(
    override val id: Long,
    override val title: String,
    override val username: String?
) : Chat
