package com.wtmberlin.ui

import androidx.lifecycle.LiveData
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
    private var _eventsTotal: MutableLiveData<String> = MutableLiveData()
    val eventsTotal: LiveData<String> = _eventsTotal
    private var _members: MutableLiveData<String> = MutableLiveData()
    val members: LiveData<String> = _members


    private val subscriptions = CompositeDisposable()

    init {
        _events2017.value = "initial value, -1"
        _events2018.value = "initial value, -1"
        _events2019.value = "initial value, -1"
        _eventsTotal.value = "initial value, -1"
        _members.value = "bluabi"

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
            repository.members()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onDataMembersLoaded)
        )

        //TODO add companies count
    }

    private fun onData2017Loaded(result: Result<List<WtmEvent>>) {
        result.data?.let {
            _events2017.value = result.data.size.toString()
        }
        result.error?.let { Timber.i(it) }
    }
    private fun onData2018Loaded(result: Result<List<WtmEvent>>) {
        result.data?.let {
            _events2018.value = result.data.size.toString()
        }
        result.error?.let { Timber.i(it) }
    }
    private fun onData2019Loaded(result: Result<List<WtmEvent>>) {
        result.data?.let {
            _events2019.value = result.data.size.toString()
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
