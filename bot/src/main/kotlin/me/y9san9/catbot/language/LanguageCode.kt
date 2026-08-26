package me.y9san9.catbot.language

enum class LanguageCode(val label: String) {
    RU(label = "\uD83C\uDDF7\u200D\uD83C\uDDFA Русский"),
    EN(label = "\uD83C\uDDFA\u200D\uD83C\uDDF8 English");

    val code = name.lowercase()
}
