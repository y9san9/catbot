package me.y9san9.catbot

import dev.inmo.tgbotapi.bot.TelegramBot
import dev.inmo.tgbotapi.extensions.api.bot.getMe
import dev.inmo.tgbotapi.extensions.api.chat.members.getChatMember
import dev.inmo.tgbotapi.extensions.utils.updates.retrieving.longPolling
import dev.inmo.tgbotapi.extensions.utils.withContent
import dev.inmo.tgbotapi.types.ChatId
import dev.inmo.tgbotapi.types.ChatMember.abstracts.AdministratorChatMember
import dev.inmo.tgbotapi.types.ChatMember.abstracts.ChatMember
import dev.inmo.tgbotapi.types.ChatMember.abstracts.LeftChatMember
import dev.inmo.tgbotapi.types.ChatMember.abstracts.MemberChatMember
import dev.inmo.tgbotapi.types.ExtendedBot
import dev.inmo.tgbotapi.types.InlineQueries.query.InlineQuery
import dev.inmo.tgbotapi.types.MessageEntity.textsources.BotCommandTextSource
import dev.inmo.tgbotapi.types.User
import dev.inmo.tgbotapi.types.abstracts.WithOptionalLanguageCode
import dev.inmo.tgbotapi.types.chat.abstracts.Chat
import dev.inmo.tgbotapi.types.chat.abstracts.PublicChat
import dev.inmo.tgbotapi.types.message.abstracts.ContentMessage
import dev.inmo.tgbotapi.types.message.abstracts.FromUserMessage
import dev.inmo.tgbotapi.types.message.abstracts.Message
import dev.inmo.tgbotapi.types.message.content.TextContent
import dev.inmo.tgbotapi.updateshandlers.FlowsUpdatesFilter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import me.y9san9.catbot.di.catgifs.CatGifsProvider
import me.y9san9.catbot.di.log.Logger
import me.y9san9.catbot.feature.botJoinedToGroup.createBotJoinedToGroupDeps
import me.y9san9.catbot.feature.startCommand.createStartCommandDeps
import me.y9san9.catbot.feature.inlineRequests.CatGifFileIdRepository
import me.y9san9.catbot.feature.inlineRequests.createInlineRequestsDeps
import me.y9san9.catbot.feature.newUserJoined.createNewUserJoinedDeps
import me.y9san9.catbot.feature.nyanCommand.createNyanCommandDeps
import me.y9san9.catbot.log.LogEvent
import me.y9san9.catbot.strings.Strings
import me.y9san9.catbot.strings.StringsProvider
import java.io.File

data class TelegramCatBotDeps(
    val scope: CoroutineScope,
    val bot: TelegramBot,
    val botJoinedEvents: Flow<PublicChat>,
    val newUsersJoinedEvents: Flow<Pair<Chat, ChatMember>>,
    val startCommandEvents: Flow<Message>,
    val nyanCommands: Flow<Message>,
    val inlineRequests: Flow<InlineQuery>,
    val logger: Logger,
    val catgifsProvider: CatGifsProvider,
    val stringsProvider: StringsProvider,
    val storage: Storage,
    val cachedGifsChatId: ChatId,
    val nyanCatFile: File
) {

    internal val deps: CatBotDeps = CatBotDeps(
        botJoinedToGroupDeps = createBotJoinedToGroupDeps(),
        newUserJoinedDeps = createNewUserJoinedDeps(),
        startCommandDeps = createStartCommandDeps(),
        inlineRequestsDeps = createInlineRequestsDeps(),
        nyanCommandDeps = createNyanCommandDeps(),
        storage = object : CatBotDeps.Storage {
            override suspend fun init() = storage.init()
        },
        logger = object : CatBotDeps.Logger {
            override fun logBotStarted() = logger.processEvent(LogEvent.BotStarted)
            override fun logBotSetupFinished() = logger.processEvent(LogEvent.SetupFinished)
        },
        scope = scope
    )

    internal val catGifFileIdRepository = CatGifFileIdRepository(bot, storage, catgifsProvider, cachedGifsChatId)
    internal suspend fun getStringsForChat(chat: Chat): Strings {
        val languageCode = storage.getLanguageCodeForChat(chat.id.chatId) ?: when (chat) {
            is User -> (chat as? WithOptionalLanguageCode)?.languageCode
            else -> null
        }
        return stringsProvider[languageCode]
    }

    internal suspend fun getStringsForMessage(message: Message): Strings =
        getStringsForChat(
            chat = (message as? FromUserMessage)
                ?.user?.takeIf { it.id == message.chat.id }
                ?: message.chat
        )
}

fun TelegramCatBotDeps(
    scope: CoroutineScope,
    bot: TelegramBot,
    logger: Logger,
    catgifsProvider: CatGifsProvider,
    stringsProvider: StringsProvider,
    storage: Storage,
    cachedGifsChatId: ChatId,
    nyanCatFile: File
): TelegramCatBotDeps {
    val me = scope.async { bot.getMe() }
    val updates = FlowsUpdatesFilter()
    bot.longPolling(flowsUpdatesFilter = updates, scope = scope)

    val botJoinedEvents = updates.myChatMemberUpdatesFlow.filter { event ->
        event.data.oldChatMemberState is LeftChatMember &&
                (event.data.newChatMemberState is MemberChatMember ||
                        event.data.newChatMemberState is AdministratorChatMember)
    }.map { event -> event.data.chat }
        .filterIsInstance<PublicChat>()

    val newUsersJoinedEvents = updates.chatMemberUpdatesFlow
        .filter { event ->
            event.data.user.id.chatId == event.data.newChatMemberState.user.id.chatId
                    || (bot.getChatMember(event.data.chat.id, event.data.user) !is AdministratorChatMember)
        }.filter { event ->
            event.data.oldChatMemberState is LeftChatMember &&
                    event.data.newChatMemberState is MemberChatMember
        }.map { event -> event.data.chat to event.data.newChatMemberState }

    val startCommandEvents = updates.messagesFlow
        .filter { event -> event.data.hasCommand(bot = me.await(), command = "start") }
        .map { event -> event.data }

    val nyanCommandEvents = updates.messagesFlow
        .filter { event -> event.data.hasCommand(bot = me.await(), command = "nyan") }
        .map { event -> event.data }

    val inlineRequests = updates.inlineQueriesFlow
        .map { event -> event.data }

    return TelegramCatBotDeps(
        scope = scope,
        bot = bot,
        botJoinedEvents = botJoinedEvents,
        newUsersJoinedEvents = newUsersJoinedEvents,
        startCommandEvents = startCommandEvents,
        nyanCommands = nyanCommandEvents,
        inlineRequests = inlineRequests,
        logger = logger,
        catgifsProvider = catgifsProvider,
        stringsProvider = stringsProvider,
        storage = storage,
        cachedGifsChatId = cachedGifsChatId,
        nyanCatFile = nyanCatFile
    )
}

private fun Message.hasCommand(bot: ExtendedBot, command: String) = (this as? ContentMessage<*>)
    ?.withContent<TextContent>()
    ?.content?.textSources?.firstOrNull()
    ?.let { it as? BotCommandTextSource }
    .let {
        it?.source == "/$command"
                || it?.source == "/$command${bot.username.username}"
    }
