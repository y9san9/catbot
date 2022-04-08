package me.y9san9.catbot.di.requests.models

import dev.inmo.tgbotapi.types.MessageEntity.textsources.TextSourcesList
import dev.inmo.tgbotapi.types.MessageEntity.textsources.italic
import dev.inmo.tgbotapi.types.MessageEntity.textsources.mention
import dev.inmo.tgbotapi.types.MessageEntity.textsources.regular
import me.y9san9.catbot.di.requests.context.UserContext

val TextEntities.asInMoTextEntities: TextSourcesList get() = map { entity ->
    when(entity) {
        is TextEntity.Mention -> mention(entity.text, (entity.user as UserContext.Telegram).id)
        is TextEntity.Regular -> regular(entity.text)
        is TextEntity.Italic -> italic(entity.text)
    }
}
