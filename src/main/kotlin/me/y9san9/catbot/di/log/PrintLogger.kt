package me.y9san9.catbot.di.log

import java.time.LocalDateTime

object PrintLogger : StringLogger() {
    override fun processEvent(event: LogEvent, stringRepresentation: String) =
        println("${LocalDateTime.now()}: $stringRepresentation")
}
