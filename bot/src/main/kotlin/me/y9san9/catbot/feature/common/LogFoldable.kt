package me.y9san9.catbot.feature.common

interface LogFoldable {
    val isSuccess: Boolean
    fun logSuccess()
    fun logFailure()
}

fun LogFoldable.fold() = when (isSuccess) {
    true -> logSuccess()
    false -> logFailure()
}

fun <T> Result<T>.logFoldable(
    success: (T) -> Unit,
    failure: (Throwable) -> Unit
): LogFoldable = object : LogFoldable {
    override val isSuccess: Boolean = this@logFoldable.isSuccess
    override fun logSuccess() = success.invoke(getOrNull()!!)
    override fun logFailure() = failure.invoke(exceptionOrNull()!!)
}
