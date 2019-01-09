package com.wtmberlin.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wtmberlin.data.Repository
import com.wtmberlin.data.Result
import com.wtmberlin.data.VenueName
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class CollaborationsViewModel(private val repository: Repository) : ViewModel() {
    val adapterItems = MutableLiveData<List<CollaborationsAdapterItem>>()
    private val subscriptions = CompositeDisposable()

    init {
        subscriptions.add(
            repository.venues()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onDataLoaded)
        )
    }


    private fun onDataLoaded(result: Result<List<VenueName>>) {
        result.data?.let { processVenues(it) }
        result.error?.let { Timber.i(it) }
    }

    private fun processVenues(list: List<VenueName>) {
        adapterItems.value = list.map {
            CollaborationsAdapterItem(it.name, it.name)
        }
    }

    override fun onCleared() {
        super.onCleared()

        subscriptions.clear()
    }
}