package me.y9san9.catbot.di.resources

import me.y9san9.catbot.di.requests.context.ChatContext

interface StringsProvider {
    val default: Strings
    val localizations: Map<LanguageCode, Strings>

    val all get() = localizations.values + default
    operator fun get(languageCode: LanguageCode?) = localizations[languageCode] ?: default
}

operator fun StringsProvider.get(chat: ChatContext) = get(LanguageCode.forChat(chat))

enum class LanguageCode {
    RU, EN, UA, BY;
    val code = name.lowercase()

    companion object {
        fun forChat(chat: ChatContext) = values()
            .firstOrNull { code -> code.code == chat.languageCode }
    }
}
