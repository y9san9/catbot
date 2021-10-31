package me.y9san9.catbot.di.resources

import me.y9san9.catbot.di.requests.models.TextEntities
import me.y9san9.catbot.di.requests.models.TextEntity

interface Strings {
    fun groupWelcomeMessage(groupTitle: String): TextEntities
    fun startMessage(): TextEntities

    fun newMemberCaptchaMessage(mention: TextEntity.Mention): TextEntities
    fun oldMemberCaptchaMessage(mention: TextEntity.Mention): TextEntities
}
