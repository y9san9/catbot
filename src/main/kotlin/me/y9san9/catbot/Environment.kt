package me.y9san9.catbot

import kotlin.system.exitProcess

private fun setupMessage(reason: String) = """
    $reason
    
    To run the bot, please setup the environment properly:
    1) BOT_TOKEN - telegram bot token
    2) DATABASE_URL - url for database to connect over jdbc, originally was tested with postgresql, so this variant is preferred
    3) DATABASE_USER - user for database
    4) DATABASE_PASSWORD - password for database
    
    Database parameters should be optional in the future (sqlite will be used as fallback)
""".trimIndent()

private fun failWithReason(reason: String): Nothing {
    System.err.println(setupMessage(reason))
    exitProcess(-1)
}

private fun failWithReasonWasNotProvided(name: String): Nothing =
    failWithReason(reason = "$name was not provided")

fun getEnvOrFail(name: String): String = System.getenv(name)
    ?: failWithReasonWasNotProvided(name)

fun getEnvOrNull(name: String): String? = System.getenv(name)
