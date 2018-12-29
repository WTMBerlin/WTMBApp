package com.wtmberlin.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

open class Event {
    var handled = false
        private set

    fun handle() {
        handled = true
    }
}

inline fun <T : Event> LiveData<T>.observeNotHandled(owner: LifecycleOwner, crossinline observer: (T) -> Unit) {
    observe(owner, Observer {
        it?.let {
            if (!it.handled) {
                it.handle()

                observer.invoke(it)
            }
        }
    })
}