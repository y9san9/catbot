package me.y9san9.catbot.di.resources

import me.y9san9.catbot.di.requests.models.TextEntities
import me.y9san9.catbot.di.requests.models.TextEntity
import me.y9san9.catbot.di.resources.DefaultStrings.Ru

object DefaultStrings {
    object Default : Strings {
        override fun groupWelcomeMessage(groupTitle: String): TextEntities =
            TextEntity.Start + "I would love to serve $groupTitle, so let's check the first cat \uD83D\uDE3B.\n\n" +
            TextEntity.Italic(text = "(add me as admin without permissions, so I would receive events about users join)")

        override fun startMessage(): TextEntities =
            TextEntity.Start + "Add me to the group and I will send there awesome cat-gifs like this \uD83D\uDC08"

        override fun newMemberCaptchaMessage(mention: TextEntity.Mention): TextEntities =
            mention + ", welcome!\nVerify that you are a human, describe this gif. You have two minutes \uD83D\uDE3A"

        override fun oldMemberCaptchaMessage(mention: TextEntity.Mention): TextEntities =
            mention + ", welcome back \uD83D\uDE3C. I remembered that you are human, but I have to send a cat right now."
    }
    object Ru : Strings {
        override fun groupWelcomeMessage(groupTitle: String): TextEntities =
            TextEntity.Start + "Буду рад служить $groupTitle! а вот и первый котик \uD83D\uDE3B\n\n" +
            TextEntity.Italic(text = "(сделайте меня администратором без прав, чтобы я получал события о присоединении пользователей)")

        override fun startMessage(): TextEntities =
            TextEntity.Start + "Добавь меня в свою группу и я буду отправлять туда куртые котогифки как эта \uD83D\uDC08"

        override fun newMemberCaptchaMessage(mention: TextEntity.Mention): TextEntities =
            mention + ", докажи, что ты человек.\nНапиши, что происходит на картинке. У тебя пара минут \uD83D\uDE3A"

        override fun oldMemberCaptchaMessage(mention: TextEntity.Mention): TextEntities =
            mention + ", рад видеть тебя снова \uD83D\uDE3C. Я запомнил, что ты человек, но в моих обязанностях отправить котика."
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
