package me.y9san9.catbot.di.storage

import org.jetbrains.exposed.sql.Database

class DatabaseStorage(
    url: String,
    user: String,
    password: String
) : Storage {
    private val db = Database.connect(
        url = url,
        user = user,
        password = password
    )
}
