package me.y9san9.catbot.di.requests.text

typealias TextEntities = List<TextEntity>

val TextEntities.raw get() = joinToString(separator = "") { it.raw }
operator fun TextEntities.plus(text: String) = plus(TextEntity.Regular(text))

sealed class TextEntity {
    abstract val raw: String

    class Regular(val text: String) : TextEntity() {
        override val raw: String = text
    }
    class Mention(val text: String, val id: Long) : TextEntity() {
        override val raw: String = text
    }
    companion object {
        val Start = Regular("")
    }

    val entities get() = listOf(this)

    operator fun plus(entity: TextEntity) = listOf(this, entity)
    operator fun plus(text: String) = listOf(this, Regular(text))
}
