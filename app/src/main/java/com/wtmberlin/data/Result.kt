package com.wtmberlin.data

data class Result<out T>(val loading: Boolean, val data: T?, val error: Throwable?)
