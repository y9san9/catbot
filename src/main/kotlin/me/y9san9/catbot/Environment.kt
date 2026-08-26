package me.y9san9.catbot

import kotlin.system.exitProcess

private fun setupMessage(reason: String) = """
    $reason
    
    To run the bot, please setup the environment properly:
    1) BOT_TOKEN - telegram bot token
    2) CACHED_GIFS_CHAT_ID - chat where the bot will send gifs to get file id. primarily used for inline requests.
    2) DATABASE_URL - url for database to connect over jdbc, originally was tested with postgresql, so this variant is preferred
    3) DATABASE_USER - user for database
    4) DATABASE_PASSWORD - password for database
    
    Optional:
    1) LOG_CHAT_ID - telegram chat/channel id where the bot will send updates to
    2) CATGIFS_PROVIDER_TYPE - local/cataas whether to use local catgifs provider (files will be picked from catgifs-tmp-dir) or make requests to cataas.com and cache up to 500 gifs in the same folder. cataas by default.
    
    Database parameters should be optional in the future (sqlite will be used as fallback)
""".trimIndent()

fun failWithReason(reason: String): Nothing {
    System.err.println(setupMessage(reason))
    exitProcess(status = -1)
}

fun failWithReasonWasNotProvided(name: String): Nothing =
    failWithReason(reason = "$name was not provided")

fun getEnvOrFail(name: String): String = System.getenv(name)
    ?: failWithReasonWasNotProvided(name)

fun getEnvOrNull(name: String): String? = System.getenv(name)

