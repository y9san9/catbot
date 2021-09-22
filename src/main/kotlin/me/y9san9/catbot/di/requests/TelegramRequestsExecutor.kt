package me.y9san9.catbot.di.requests

import dev.inmo.tgbotapi.bot.Ktor.telegramBot
import dev.inmo.tgbotapi.bot.TelegramBot
import dev.inmo.tgbotapi.extensions.api.send.media.sendAnimation
import dev.inmo.tgbotapi.extensions.utils.asWithOptionalLanguageCode
import dev.inmo.tgbotapi.extensions.utils.shortcuts.newGroupMembersEvents
import dev.inmo.tgbotapi.extensions.utils.updates.retrieving.longPollingFlow
import dev.inmo.tgbotapi.requests.abstracts.MultipartFile
import dev.inmo.tgbotapi.types.ChatId
import dev.inmo.tgbotapi.types.ChatMember.abstracts.LeftChatMember
import dev.inmo.tgbotapi.types.ChatMember.abstracts.MemberChatMember
import dev.inmo.tgbotapi.types.abstracts.WithOptionalLanguageCode
import dev.inmo.tgbotapi.types.update.CommonChatMemberUpdatedUpdate
import dev.inmo.tgbotapi.updateshandlers.FlowsUpdatesFilter
import dev.inmo.tgbotapi.utils.StorageFile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import me.y9san9.catbot.di.requests.text.TextEntities
import me.y9san9.catbot.di.requests.text.asInMoTextEntities
import me.y9san9.catbot.di.requests.user.ChatMember
import java.io.File

fun TelegramRequestsExecutor(
    scope: CoroutineScope,
    botToken: String
): TelegramRequestsExecutor {
    val bot = telegramBot(botToken)
    val flows = FlowsUpdatesFilter()

    bot.longPollingFlow()
        .onEach(flows.asUpdateReceiver)
        .launchIn(scope)

    return TelegramRequestsExecutor(bot, flows)
}

class TelegramRequestsExecutor(
    private val bot: TelegramBot,
    flows: FlowsUpdatesFilter
) : RequestsExecutor {

    override val newMembersSelfJoined: Flow<ChatMember> =
        flows.chatMemberUpdatesFlow
            .filter { event ->
                event.data.oldChatMemberState is LeftChatMember &&
                    event.data.newChatMemberState is MemberChatMember
            }
            .map { event ->
                val member = event.data.user

                return@map ChatMember(
                    id = member.id.chatId,
                    firstName = member.firstName,
                    lastName = member.lastName,
                    languageCode = (member as? WithOptionalLanguageCode)?.languageCode,
                    chatId = event.data.chat.id.chatId
                )
            }

    override suspend fun sendGif(chatId: Long, text: TextEntities, gif: File) = bot.sendAnimation(
        chatId = ChatId(chatId),
        animation = MultipartFile(file = StorageFile(gif)),
        entities = text.asInMoTextEntities
    ).let {}
}
