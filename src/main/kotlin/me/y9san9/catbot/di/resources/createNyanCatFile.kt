package me.y9san9.catbot.di.resources

import java.io.File
import java.nio.file.Files


private class NyanCat

fun createNyanCatFile(): File {
    val file = File.createTempFile("nyancatbot", ".gif")
    val resource = NyanCat::class.java.classLoader.getResourceAsStream("nyan.gif") ?: error("Nyan gif not found :(")
    resource.use { stream -> stream.copyTo(file.outputStream()) }
    return file
}
