package com.wtmberlin.data

sealed class Result<out T : Any> {
    data class Success<out T : Any>(val data: T) : Result<T>()
    data class Error<out T : Any>(val exception: Throwable) : Result<T>()

    companion object {
        fun <T : Any> success(data: T): Result<T> = Success(data)
        fun <T : Any> error(exception: Throwable): Result<T> = Error(exception)
    }
}