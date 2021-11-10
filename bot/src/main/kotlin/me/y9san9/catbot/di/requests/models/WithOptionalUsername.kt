package me.y9san9.catbot.di.requests.models

interface WithOptionalUsername {
    val username: Username?

    val link: String? get() = username?.let { username ->
        "https://t.me/${username.withoutPrefix}"
    }
}
