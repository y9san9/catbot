package me.y9san9.catbot.di.resources

import dev.inmo.tgbotapi.types.MessageEntity.textsources.*
import dev.inmo.tgbotapi.types.chat.abstracts.PublicChat
import me.y9san9.catbot.strings.Strings
import me.y9san9.catbot.strings.StringsProvider

object DefaultStrings {
    object Default : Strings {
        override fun groupWelcomeMessage(chat: PublicChat): TextSourcesList =
            regular("I would love to serve ${chat.title}, so let's check the first cat \uD83D\uDE3B.\n\n") +
            italic("(add me as admin without permissions, so I would receive events about users join)")

        override fun startMessage(): TextSourcesList =
            listOf(regular("Add me to the group and I will send there awesome cat-gifs like this \uD83D\uDC08"))

        override fun newMemberCaptchaMessage(mention: TextMentionTextSource): TextSourcesList =
            mention + ", welcome!\nVerify that you are a human, describe this gif. You have two minutes \uD83D\uDE3A"

        override fun oldMemberCaptchaMessage(mention: TextMentionTextSource): TextSourcesList =
            mention + ", welcome back \uD83D\uDE3C. I remembered that you are human, but I have to send a cat right now."
    }
    object RU : Strings {
        override fun groupWelcomeMessage(chat: PublicChat): TextSourcesList =
            regular("Буду рад служить ${chat.title}! а вот и первый котик \uD83D\uDE3B\n\n") +
            italic(text = "(сделайте меня администратором без прав, чтобы я получал события о присоединении пользователей)")

        override fun startMessage(): TextSourcesList =
            listOf(regular("Добавь меня в свою группу и я буду отправлять туда куртые котогифки как эта \uD83D\uDC08"))

        override fun newMemberCaptchaMessage(mention: TextMentionTextSource): TextSourcesList =
            mention + ", докажи, что ты человек.\nНапиши, что происходит на картинке. У тебя пара минут \uD83D\uDE3A"

        override fun oldMemberCaptchaMessage(mention: TextMentionTextSource): TextSourcesList =
            mention + ", рад видеть тебя снова \uD83D\uDE3C. Я запомнил, что ты человек, но в моих обязанностях отправить котика."
    }
}

object DefaultStringsProvider : StringsProvider {
    override fun get(languageCode: String?): Strings = when (languageCode?.lowercase()) {
        "ru", "ua", "by" -> DefaultStrings.RU
        else -> DefaultStrings.Default
    }
}
