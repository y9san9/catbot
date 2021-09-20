package me.y9san9.catbot.di.resources

import me.y9san9.catbot.di.requests.text.TextEntities
import me.y9san9.catbot.di.requests.text.TextEntity
import me.y9san9.catbot.di.resources.DefaultStrings.Ru

object DefaultStrings {
    object Default : Strings {
        override fun newMemberCaptchaMessage(mention: TextEntity.Mention): TextEntities =
            mention + ", welcome!\nVerify that you are a human, describe this gif. You have two minutes \uD83D\uDE3A"
    }
    object Ru : Strings {
        override fun newMemberCaptchaMessage(mention: TextEntity.Mention): TextEntities =
            mention + ", докажи, что ты человек.\nНапиши, что происходит на картнке. У тебя пара минут \uD83D\uDE3A"
    }
}

object DefaultStringsProvider : StringsProvider {
    override val default: Strings = DefaultStrings.Default
    override val localizations: Map<String, Strings> = mapOf(
        "ru" to Ru,
        "ua" to Ru,
        "by" to Ru
    )
}
