package me.y9san9.catgifs.cataas.internal.extensions.byteReadChannel

import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.core.isNotEmpty
import io.ktor.utils.io.core.readBytes
import io.ktor.utils.io.readRemaining
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.flow.toList

internal fun ByteReadChannel.readAsFlow(bufferSize: Int = DEFAULT_BUFFER_SIZE): Flow<ByteArray> = flow {
    while(!isClosedForRead) {
        val packet = readRemaining(bufferSize.toLong())
        while(packet.isNotEmpty) {
            emit(packet.readBytes())
        }
    }
}
