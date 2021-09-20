package me.y9san9.catbot.di.resources

import me.y9san9.catbot.di.requests.text.TextEntities
import me.y9san9.catbot.di.requests.text.TextEntity

interface Strings {
    fun newMemberCaptchaMessage(mention: TextEntity.Mention): TextEntities
}
