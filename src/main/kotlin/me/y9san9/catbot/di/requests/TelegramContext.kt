package me.y9san9.catbot.di.requests

import dev.inmo.micro_utils.coroutines.safelyWithoutExceptions
import dev.inmo.tgbotapi.bot.TelegramBot
import dev.inmo.tgbotapi.extensions.api.bot.getMe
import dev.inmo.tgbotapi.extensions.api.chat.members.getChatMember
import dev.inmo.tgbotapi.extensions.api.send.media.sendAnimation
import dev.inmo.tgbotapi.extensions.utils.updates.retrieving.longPolling
import dev.inmo.tgbotapi.extensions.utils.withContent
import dev.inmo.tgbotapi.requests.abstracts.MultipartFile
import dev.inmo.tgbotapi.types.ChatId
import dev.inmo.tgbotapi.types.ChatMember.abstracts.AdministratorChatMember
import dev.inmo.tgbotapi.types.ChatMember.abstracts.LeftChatMember
import dev.inmo.tgbotapi.types.ChatMember.abstracts.MemberChatMember
import dev.inmo.tgbotapi.types.MessageEntity.textsources.BotCommandTextSource
import dev.inmo.tgbotapi.types.chat.abstracts.PublicChat
import dev.inmo.tgbotapi.types.message.abstracts.ContentMessage
import dev.inmo.tgbotapi.types.message.abstracts.Message
import dev.inmo.tgbotapi.types.message.content.TextContent
import dev.inmo.tgbotapi.updateshandlers.FlowsUpdatesFilter
import dev.inmo.tgbotapi.utils.StorageFile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import me.y9san9.catbot.di.requests.context.CatbotContext
import me.y9san9.catbot.di.requests.models.Chat
import me.y9san9.catbot.di.requests.models.asInMoTextEntities
import me.y9san9.catbot.di.requests.models.ChatMember
import me.y9san9.catbot.di.requests.models.TextEntities
import me.y9san9.catbot.di.requests.models.asChat
import java.io.File

fun TelegramRequestsExecutor(scope: CoroutineScope, bot: TelegramBot): TelegramContext {
    val flows = FlowsUpdatesFilter()

    val executor = TelegramContext(bot, flows, scope)
    bot.longPolling(flows, scope = scope)

    return executor
}

class TelegramContext(
    private val bot: TelegramBot,
    flows: FlowsUpdatesFilter,
    scope: CoroutineScope
) : CatbotContext {
    private val me = scope.async { bot.getMe() }

    override val newMembersJoined: Flow<ChatMember> =
        flows.chatMemberUpdatesFlow
            .filter { event ->
                event.data.user.id.chatId == event.data.newChatMemberState.user.id.chatId
                        || (bot.getChatMember(event.data.chat.id, event.data.user) !is AdministratorChatMember)
            }.filter { event ->
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
        flows.messagesFlow
            .filter { event -> event.data.hasCommand(command = "start") }
            .map { event -> event.data.chat.asChat }

    override suspend fun sendGif(chat: Chat, text: TextEntities, gif: File): Boolean =
        safelyWithoutExceptions {
            bot.sendAnimation(
                chatId = ChatId(chat.id),
                animation = MultipartFile(file = StorageFile(gif)),
                entities = text.asInMoTextEntities
            )
        } != null

    private suspend fun Message.hasCommand(command: String) = (this as? ContentMessage<*>)
        ?.withContent<TextContent>()
        ?.content?.textSources?.firstOrNull()
        ?.let { it as? BotCommandTextSource }
        .let {
            it?.source == "/$command"
                    || it?.source == "/$command${me.await().username.username}"
        }
}
