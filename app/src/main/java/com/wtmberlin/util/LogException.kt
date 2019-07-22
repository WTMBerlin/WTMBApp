package com.wtmberlin.util

import com.wtmberlin.BuildConfig
import timber.log.Timber

class LogException {

    fun getException(e: Throwable) {
        if (BuildConfig.DEBUG) {
            Timber.i(e)
        } else {
            //TODO: add a release crash lib
        }
    }

}