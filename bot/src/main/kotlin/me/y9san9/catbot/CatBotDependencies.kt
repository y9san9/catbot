package me.y9san9.catbot

import kotlinx.coroutines.CoroutineScope
import me.y9san9.catbot.di.catgifs.CatGifsProvider
import me.y9san9.catbot.di.log.Logger
import me.y9san9.catbot.di.requests.context.CatbotContext
import me.y9san9.catbot.di.resources.StringsProvider
import me.y9san9.catbot.di.storage.Storage

class CatBotDependencies(
    val executor: CatbotContext,
    val catGifs: CatGifsProvider,
    val stringsProvider: StringsProvider,
    val storage: Storage,
    val logger: Logger,
    val scope: CoroutineScope
)
