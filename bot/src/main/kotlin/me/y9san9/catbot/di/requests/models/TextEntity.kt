package me.y9san9.catbot.di.requests.models

import me.y9san9.catbot.di.requests.context.UserContext

typealias TextEntities = List<TextEntity>

val TextEntities.raw get() = joinToString(separator = "") { it.raw }
operator fun TextEntities.plus(text: String) = plus(TextEntity.Regular(text))

sealed class TextEntity {
    abstract val raw: String

    class Regular(val text: String) : TextEntity() {
        override val raw: String = text
    }
    class Italic(val text: String) : TextEntity() {
        override val raw: String = text
    }
    class Mention(val text: String, val user: UserContext) : TextEntity() {
        override val raw: String = text
    }

    companion object {
        val Start = Regular(text = "")
    }

    val asList get() = listOf(this)

    operator fun plus(entity: TextEntity) = listOf(this, entity)
    operator fun plus(text: String) = listOf(this, Regular(text))
}
