package com.wtmberlin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wtmberlin.data.DetailedWtmEvent
import com.wtmberlin.data.Repository
import com.wtmberlin.data.Result
import com.wtmberlin.data.WtmEvent
import com.wtmberlin.util.exhaustive
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class EventDetailsViewModel(private val eventId: String, private val repository: Repository): ViewModel() {
    val event = MutableLiveData<DetailedWtmEvent>()

    private val subscriptions = CompositeDisposable()

    init {
        subscriptions.add(repository.event(eventId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::onDataLoaded))
    }

    private fun onDataLoaded(result: Result<DetailedWtmEvent>) {
        when (result) {
            is Result.Success<DetailedWtmEvent> -> event.value = result.data
            is Result.Error<*> -> Timber.w(result.exception)
        }.exhaustive
    }

    override fun onCleared() {
        super.onCleared()

        subscriptions.clear()
    }
}