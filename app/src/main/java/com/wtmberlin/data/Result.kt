package com.wtmberlin.data

data class Result<out T>(val loading: Boolean, val data: T?, val error: Throwable?)

suspend fun <T> runCatching(block: suspend () -> Result<T>) = try {
    block()
} catch (e: Exception) {
    Result(loading = false, data = null, error = e)
}