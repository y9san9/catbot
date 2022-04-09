package me.y9san9.catbot.strings

import dev.inmo.tgbotapi.types.MessageEntity.textsources.TextMentionTextSource
import dev.inmo.tgbotapi.types.MessageEntity.textsources.TextSourcesList
import dev.inmo.tgbotapi.types.chat.abstracts.PublicChat

interface Strings {
    fun groupWelcomeMessage(chat: PublicChat): TextSourcesList
    fun newMemberCaptchaMessage(mention: TextMentionTextSource): TextSourcesList
    fun oldMemberCaptchaMessage(mention: TextMentionTextSource): TextSourcesList
    fun startMessage(): TextSourcesList
}
