package com.wtmberlin.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wtmberlin.data.Repository
import com.wtmberlin.data.Result
import com.wtmberlin.data.WtmEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class StatsViewModel(repository: Repository) : ViewModel() {
    private var _events2017: MutableLiveData<Int> = MutableLiveData()
    val events2017: LiveData<Int> = _events2017

    private val subscriptions = CompositeDisposable()

    init {
        _events2017.value = 0
        subscriptions.add(
            repository.events2017()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onDataLoaded)
        )
    }

    private fun onDataLoaded(result: Result<List<WtmEvent>>) {
        result.data?.let {
            _events2017.value = result.data.size
        }
        result.error?.let { Timber.i(it) }
    }


    override fun onCleared() {
        super.onCleared()
        subscriptions.clear()
    }
}
