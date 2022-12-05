package me.y9san9.catbot.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.math.BigInteger
import java.security.MessageDigest


suspend fun File.getMD5() =
    withContext(Dispatchers.IO) {
        getMD5EncryptedString(inputStream())
    }

private const val STREAM_BUFFER_LENGTH = 1024

fun getMD5EncryptedString(fis: FileInputStream): String {
    val mdEnc = MessageDigest.getInstance("MD5")
    mdEnc.update(fis)
    var md5: String = BigInteger(1, mdEnc.digest()).toString(16)
    while (md5.length < 32) {
        md5 = "0$md5"
    }
    return md5
}

private fun MessageDigest.update(data: InputStream) {
    val buffer = ByteArray(STREAM_BUFFER_LENGTH)
    var read = data.read(buffer, 0, STREAM_BUFFER_LENGTH)
    while (read > -1) {
        update(buffer, 0, read)
        read = data.read(buffer, 0, STREAM_BUFFER_LENGTH)
    }
}

