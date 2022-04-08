package me.y9san9.catbot.utils

import java.io.File
import java.math.BigInteger
import java.security.MessageDigest


fun File.getMD5ForSmallData() = getMD5EncryptedStringForSmallData(readBytes())

fun getMD5EncryptedStringForSmallData(bytes: ByteArray): String {
    val mdEnc = MessageDigest.getInstance("MD5")
    mdEnc.update(bytes, 0, bytes.size)
    var md5: String = BigInteger(1, mdEnc.digest()).toString(16)
    while (md5.length < 32) {
        md5 = "0$md5"
    }
    return md5
}
