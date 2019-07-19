package com.wtmberlin.util

import timber.log.Timber

class ErrorLogger {

    fun getException(e: Throwable) {
        Timber.i(e)
    }

}