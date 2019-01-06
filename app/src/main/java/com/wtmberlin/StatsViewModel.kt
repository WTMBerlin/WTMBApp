package com.wtmberlin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wtmberlin.data.Result
import com.wtmberlin.data.Repository
import com.wtmberlin.data.WtmGroup
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

/**
 *
 * This class displays following info about the community:
 * membersCount
 * events2017
 * events2018
 * events2019
 * eventsTotal
 * */

class StatsViewModel(private val repository: Repository) : ViewModel() {
    //TODO implement VM
    val stats = MutableLiveData<WtmGroup>()

    private val subscriptions = CompositeDisposable()

    init {
        subscriptions.add(
            repository.group()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onDataLoaded)
        )
    }

    private fun onDataLoaded(result: Result<WtmGroup>) {
        result.data?.let { stats.value = it }
        result.error?.let { Timber.i(it) }
    }

    override fun onCleared() {
        super.onCleared()
        subscriptions.clear()
    }
}