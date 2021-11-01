package me.y9san9.catbot.di.requests.models

interface WithOptionalUsername {
    val username: String?

    val link: String? get() = username?.let { username ->
        "https://t.me/${username.replaceFirst("@", "")}"
    }
}
