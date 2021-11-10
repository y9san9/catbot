package me.y9san9.catbot.di.requests.models

interface User : Chat, WithOptionalLanguageCode, WithOptionalUsername {
    override val id: Long

    val firstName: String
    val lastName: String

    override val username: Username?

    override val title: String get() = lastName
        .takeIf { it.isNotBlank() }
        ?.let { "$firstName $lastName" }
        ?: firstName

    fun mention(title: String): TextEntity.Mention = TextEntity.Mention(title, id)
    val mention get() = mention(title)
}

fun User(
    languageCode: String?,
    id: Long,
    firstName: String,
    lastName: String,
    username: Username?
): User = _User(languageCode, id, firstName, lastName, username)

@Suppress("ClassName")
private data class _User(
    override val languageCode: String?,
    override val id: Long,
    override val firstName: String,
    override val lastName: String,
    override val username: Username?
) : User
