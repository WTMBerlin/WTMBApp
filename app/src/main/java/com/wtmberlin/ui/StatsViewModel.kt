package com.wtmberlin.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wtmberlin.data.Repository
import com.wtmberlin.data.Result
import com.wtmberlin.data.WtmEvent
import com.wtmberlin.meetup.MeetupMembers
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber


class StatsViewModel(repository: Repository) : ViewModel() {
    private var _events2017: MutableLiveData<String> = MutableLiveData()
    val events2017: LiveData<String> = _events2017
    private var _events2018: MutableLiveData<String> = MutableLiveData()
    val events2018: LiveData<String> = _events2018
    private var _events2019: MutableLiveData<String> = MutableLiveData()
    val events2019: LiveData<String> = _events2019
    private var _events2020: MutableLiveData<String> = MutableLiveData()
    val events2020: LiveData<String> = _events2020
    private var _eventsTotal: MutableLiveData<String> = MutableLiveData()
    val eventsTotal: LiveData<String> = _eventsTotal
    private var _members: MutableLiveData<String> = MutableLiveData()
    val members: LiveData<String> = _members

    private val subscriptions = CompositeDisposable()

    init {
        _events2017.value = "0"
        _events2018.value = "0"
        _events2019.value = "0"
        _events2020.value = "0"
        _eventsTotal.value = "0"
        _members.value = "0"

        subscriptions.add(
            repository.events2017()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onData2017Loaded)
        )

        subscriptions.add(
            repository.events2018()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onData2018Loaded)
        )
        subscriptions.add(
            repository.events2019()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onData2019Loaded)
        )
        subscriptions.add(
            repository.events2020()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onData2020Loaded)
        )
        subscriptions.add(
            repository.members()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onDataMembersLoaded)
        )

    }

    private fun onData2017Loaded(result: Result<List<WtmEvent>>) {
        result.data?.let {
            val eventCount = result.data.size
            _events2017.value = eventCount.toString()
            _eventsTotal.value = (_eventsTotal.value!!.toInt() + eventCount).toString()
        }
        result.error?.let { Timber.i(it) }
    }

    private fun onData2018Loaded(result: Result<List<WtmEvent>>) {
        result.data?.let {
            val eventCount = result.data.size
            _events2018.value = eventCount.toString()
            _eventsTotal.value = (_eventsTotal.value!!.toInt() + eventCount).toString()
        }
        result.error?.let { Timber.i(it) }
    }

    private fun onData2019Loaded(result: Result<List<WtmEvent>>) {
        result.data?.let {
            val eventCount = result.data.size
            _events2019.value = eventCount.toString()
            _eventsTotal.value = (_eventsTotal.value!!.toInt() + eventCount).toString()
        }
        result.error?.let { Timber.i(it) }
    }
    private fun onData2020Loaded(result: Result<List<WtmEvent>>) {
        result.data?.let {
            val eventCount = result.data.size
            _events2020.value = eventCount.toString()
            _eventsTotal.value = (_eventsTotal.value!!.toInt() + eventCount).toString()
        }
        result.error?.let { Timber.i(it) }
    }

    private fun onDataMembersLoaded(result: Result<MeetupMembers>) {
        result.data?.let {
            _members.value = result.data.members.toString()
        }
        result.error?.let { Timber.i(it) }
    }


    override fun onCleared() {
        super.onCleared()
        subscriptions.clear()
    }
}
