package me.y9san9.catbot.strings

interface StringsProvider {
    operator fun get(languageCode: String?): Strings
}
