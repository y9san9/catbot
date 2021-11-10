package me.y9san9.catbot.di.requests.models

@JvmInline
value class Username(private val value: String) {
    val withPrefix: String get() = value
    val withoutPrefix: String get() = value.drop(n = 1)
}
