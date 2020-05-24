package com.wtmberlin.util

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

var ui: CoroutineDispatcher = Dispatchers.Main
var io: CoroutineDispatcher = Dispatchers.IO
var background: CoroutineDispatcher = Dispatchers.Default

abstract class CoroutineViewModel : ViewModel(), CoroutineScope {


    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = job + ui

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

}