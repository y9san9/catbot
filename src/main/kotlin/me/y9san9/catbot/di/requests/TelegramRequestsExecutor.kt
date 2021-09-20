package me.y9san9.catbot.di.requests

import dev.inmo.tgbotapi.bot.Ktor.telegramBot
import dev.inmo.tgbotapi.bot.TelegramBot
import dev.inmo.tgbotapi.extensions.api.send.media.sendAnimation
import dev.inmo.tgbotapi.extensions.utils.shortcuts.newGroupMembersEvents
import dev.inmo.tgbotapi.extensions.utils.updates.retrieving.longPollingFlow
import dev.inmo.tgbotapi.requests.abstracts.MultipartFile
import dev.inmo.tgbotapi.types.ChatId
import dev.inmo.tgbotapi.updateshandlers.FlowsUpdatesFilter
import dev.inmo.tgbotapi.utils.StorageFile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
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
        flows.newGroupMembersEvents()
            .mapNotNull { event ->
                // Check if the event was produced by person joined
                val member = event.chatEvent.members.getOrNull(index = 0)
                    ?.takeIf { it.id == event.user.id }
                    ?: return@mapNotNull null

                return@mapNotNull ChatMember(
                    id = member.id.chatId,
                    firstName = member.firstName,
                    lastName = member.lastName,
                    chatId = event.chat.id.chatId
                )
            }

    override suspend fun sendGif(chatId: Long, text: TextEntities, gif: File) = bot.sendAnimation(
        chatId = ChatId(chatId),
        animation = MultipartFile(file = StorageFile(gif)),
        entities = text.asInMoTextEntities
    ).let {}
}
