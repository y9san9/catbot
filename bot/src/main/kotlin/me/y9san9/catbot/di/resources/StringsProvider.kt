package me.y9san9.catbot.di.resources

interface StringsProvider {
    val default: Strings
    val localizations: Map<String, Strings>

    val all get() = localizations.values + default
    operator fun get(languageCode: String?) = localizations[languageCode] ?: default
}
