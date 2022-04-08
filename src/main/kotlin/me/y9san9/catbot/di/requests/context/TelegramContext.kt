package me.y9san9.catbot.di.requests.context

import dev.inmo.tgbotapi.bot.TelegramBot
import dev.inmo.tgbotapi.extensions.api.bot.getMe
import dev.inmo.tgbotapi.extensions.api.chat.members.getChatMember
import dev.inmo.tgbotapi.extensions.utils.updates.retrieving.longPolling
import dev.inmo.tgbotapi.extensions.utils.withContent
import dev.inmo.tgbotapi.requests.abstracts.FileId
import dev.inmo.tgbotapi.types.ChatMember.abstracts.AdministratorChatMember
import dev.inmo.tgbotapi.types.ChatMember.abstracts.LeftChatMember
import dev.inmo.tgbotapi.types.ChatMember.abstracts.MemberChatMember
import dev.inmo.tgbotapi.types.MessageEntity.textsources.BotCommandTextSource
import dev.inmo.tgbotapi.types.message.abstracts.ContentMessage
import dev.inmo.tgbotapi.types.message.abstracts.Message
import dev.inmo.tgbotapi.types.message.content.TextContent
import dev.inmo.tgbotapi.updateshandlers.FlowsUpdatesFilter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import java.io.File

fun TelegramContext(
    scope: CoroutineScope,
    bot: TelegramBot,
    storage: TelegramContext.FileIdStorage,
    cachedGifsChatId: Long
): TelegramContext {
    val flows = FlowsUpdatesFilter()

    val executor = TelegramContext(bot, flows, storage, cachedGifsChatId, scope)
    bot.longPolling(flows, scope = scope)

    return executor
}

class TelegramContext(
    private val bot: TelegramBot,
    flows: FlowsUpdatesFilter,
    private val fileIdStorage: FileIdStorage,
    private val cachedGifsChatId: Long,
    scope: CoroutineScope
) : CatbotContext.Telegram {
    private val me = scope.async { bot.getMe() }

    override val newMembersJoined: Flow<ChatMemberContext.Telegram> =
        flows.chatMemberUpdatesFlow
            .filter { event ->
                event.data.user.id.chatId == event.data.newChatMemberState.user.id.chatId
                        || (bot.getChatMember(event.data.chat.id, event.data.user) !is AdministratorChatMember)
            }.filter { event ->
                event.data.oldChatMemberState is LeftChatMember &&
                        event.data.newChatMemberState is MemberChatMember
            }.map { event ->
                TelegramChatMemberContext(bot, event.data.newChatMemberState.user, event.data.chat, fileIdStorage)
            }

    override val botJoinedToGroup: Flow<ChatContext.Telegram> =
        flows.myChatMemberUpdatesFlow.filter { event ->
            event.data.oldChatMemberState is LeftChatMember &&
                    (event.data.newChatMemberState is MemberChatMember ||
                            event.data.newChatMemberState is AdministratorChatMember)
        }.map { event -> TelegramChatContext(bot, event.data.chat, fileIdStorage) }

    override val startCommands: Flow<MessageContext.Telegram> =
        flows.messagesFlow
            .filter { event -> event.data.hasCommand(command = "start") }
            .map { event -> TelegramMessageContext(bot, event.data, fileIdStorage) }

    override val inlineRequests: Flow<InlineRequestContext.Telegram> =
        flows.inlineQueriesFlow.map { update ->
            TelegramInlineRequestContext(bot, update.data, fileIdStorage, cachedGifsChatId)
        }

    private suspend fun Message.hasCommand(command: String) = (this as? ContentMessage<*>)
        ?.withContent<TextContent>()
        ?.content?.textSources?.firstOrNull()
        ?.let { it as? BotCommandTextSource }
        .let {
            it?.source == "/$command"
                    || it?.source == "/$command${me.await().username.username}"
        }

    interface FileIdStorage {
        fun getFileId(file: File): FileId?
        fun putFileId(file: File, id: FileId)
    }
}
