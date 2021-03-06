package me.y9san9.catbot.di.log

import java.time.LocalDateTime

object PrintLogger : LogWriter {
    override fun log(message: String) = println("${LocalDateTime.now()}: $message")
}
