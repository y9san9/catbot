package me.y9san9.catbot

private fun setupMessage(reason: String) = """
    $reason
    To run the bot, please setup the environment properly:
    1) BOT_TOKEN: env - telegram bot token
""".trimIndent()
//2) DATABASE_URL: env - database url to be connected with jdbc. Originally tested with postgres, so this variant is preferred.
//3) DATABASE_PASSWORD: env - optional, if the password was set

private fun failWithReason(reason: String): Nothing = error(setupMessage(reason))

private fun failWithReasonWasNotProvided(name: String): Nothing = failWithReason(
        reason = "$name was not provided"
)

fun getEnvOrFail(name: String): String = System.getenv(name)
        ?: failWithReasonWasNotProvided(name)

fun getEnvOrNull(name: String): String? = System.getenv(name)
