package me.y9san9.catbot.di.requests

import dev.inmo.tgbotapi.bot.Ktor.telegramBot
import dev.inmo.tgbotapi.bot.TelegramBot
import dev.inmo.tgbotapi.extensions.api.send.media.sendAnimation
import dev.inmo.tgbotapi.extensions.utils.extensions.parseCommandsWithParams
import dev.inmo.tgbotapi.extensions.utils.updates.retrieving.longPolling
import dev.inmo.tgbotapi.extensions.utils.withContent
import dev.inmo.tgbotapi.requests.abstracts.MultipartFile
import dev.inmo.tgbotapi.types.ChatId
import dev.inmo.tgbotapi.types.ChatMember.abstracts.AdministratorChatMember
import dev.inmo.tgbotapi.types.ChatMember.abstracts.LeftChatMember
import dev.inmo.tgbotapi.types.ChatMember.abstracts.MemberChatMember
import dev.inmo.tgbotapi.types.chat.abstracts.PublicChat
import dev.inmo.tgbotapi.types.link
import dev.inmo.tgbotapi.types.message.abstracts.ContentMessage
import dev.inmo.tgbotapi.types.message.abstracts.FromUserMessage
import dev.inmo.tgbotapi.types.message.content.TextContent
import dev.inmo.tgbotapi.updateshandlers.FlowsUpdatesFilter
import dev.inmo.tgbotapi.utils.StorageFile
import kotlinx.coroutines.flow.*
import me.y9san9.catbot.di.requests.models.Chat
import me.y9san9.catbot.di.requests.models.asInMoTextEntities
import me.y9san9.catbot.di.requests.models.ChatMember
import me.y9san9.catbot.di.requests.models.TextEntities
import me.y9san9.catbot.di.requests.models.asChat
import me.y9san9.catbot.util.unit
import java.io.File

fun TelegramRequestsExecutor(bot: TelegramBot): TelegramRequestsExecutor {
    val flows = FlowsUpdatesFilter()

    val executor = TelegramRequestsExecutor(bot, flows)

    bot.longPolling(flows)

    return executor
}

class TelegramRequestsExecutor(
    private val bot: TelegramBot,
    flows: FlowsUpdatesFilter
) : CatbotRequestsExecutor {
    override val newMembersJoined: Flow<ChatMember> =
        flows.chatMemberUpdatesFlow
            .filter { event -> event.data.user.id.chatId == event.data.newChatMemberState.user.id.chatId }
            .filter { event ->
                event.data.oldChatMemberState is LeftChatMember &&
                    event.data.newChatMemberState is MemberChatMember
            }
            .map { event -> ChatMember(event.data.user, event.data.chat as PublicChat) }

    override val botJoinedToGroup: Flow<Chat> =
        flows.myChatMemberUpdatesFlow.filter { event ->
            event.data.oldChatMemberState is LeftChatMember &&
                    (event.data.newChatMemberState is MemberChatMember ||
                            event.data.newChatMemberState is AdministratorChatMember)
        }.map { event -> event.data.chat.asChat }

    override val startCommands: Flow<Chat> =
        flows.messagesFlow.filter { event ->
            val message = (event.data as? ContentMessage<*>)
                ?.withContent<TextContent>()
                ?.parseCommandsWithParams()
                ?.keys?.singleOrNull() ?: return@filter false
            message == "start"
        }.map { event -> event.data.chat.asChat }

    override suspend fun sendGif(chatId: Long, text: TextEntities, gif: File) = bot.sendAnimation(
        chatId = ChatId(chatId),
        animation = MultipartFile(file = StorageFile(gif)),
        entities = text.asInMoTextEntities
    ).unit
}
