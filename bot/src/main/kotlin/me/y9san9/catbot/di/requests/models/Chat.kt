package me.y9san9.catbot.di.requests.models

interface Chat : WithOptionalUsername {
    val id: Long
    val title: String
    override val username: Username?
}

fun Chat(
    id: Long,
    title: String,
    username: Username?
): Chat = _Chat(id, title, username)

@Suppress("ClassName")
private data class _Chat(
    override val id: Long,
    override val title: String,
    override val username: Username?
) : Chat
