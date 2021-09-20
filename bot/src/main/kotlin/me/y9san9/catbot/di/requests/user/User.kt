package me.y9san9.catbot.di.requests.user

import me.y9san9.catbot.di.requests.language.PossiblyHasLanguageCode
import me.y9san9.catbot.di.requests.text.TextEntity

interface User : PossiblyHasLanguageCode {
    val id: Long

    val firstName: String
    val lastName: String

    val title: String get() = lastName
        .takeIf { it.isNotBlank() }
        ?.let { "$firstName $lastName" }
        ?: firstName

    val mention: TextEntity.Mention get() = TextEntity.Mention(title, id)
}
