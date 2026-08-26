package me.y9san9.catbot.strings

import dev.inmo.tgbotapi.types.chat.PublicChat
import dev.inmo.tgbotapi.types.message.textsources.TextMentionTextSource
import dev.inmo.tgbotapi.types.message.textsources.TextSourcesList

interface Strings {
    fun groupWelcomeMessage(chat: PublicChat): TextSourcesList
    fun newMemberCaptchaMessage(mention: TextMentionTextSource): TextSourcesList
    fun oldMemberCaptchaMessage(mention: TextMentionTextSource): TextSourcesList
    fun startMessage(): TextSourcesList
}
