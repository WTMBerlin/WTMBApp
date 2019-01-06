package com.wtmberlin.data

import com.wtmberlin.data.RefreshStatus.*
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.processors.BehaviorProcessor
import io.reactivex.processors.PublishProcessor
import io.reactivex.schedulers.Schedulers

abstract class NetworkBoundResource<T> {
    private val refreshEvents = PublishProcessor.create<RefreshEvent>()

    private val refreshStatuses = BehaviorProcessor.create<RefreshStatus>()

    init {
        refreshEvents
            .flatMapCompletable { doRefresh() }
            .subscribe()

        refreshStatuses.onNext(Idle)
    }

    fun refresh() {
        refreshEvents.onNext(RefreshEvent)
    }

    fun values(): Flowable<Result<T>> {
        return Flowable.combineLatest(
            loadFromDatabase(),
            refreshStatuses,
            BiFunction { data: T, refreshStatus: RefreshStatus -> toResult(data, refreshStatus) }
        )
            .doOnSubscribe { refresh() }
            .onErrorReturn { Result<T>(loading = false, data = null, error = it) }
    }

    private fun toResult(data: T, refreshStatus: RefreshStatus) =
        when (refreshStatus) {
            is Idle -> Result(loading = false, data = data, error = null)
            is InProgress -> Result(loading = true, data = data, error = null)
            is Error -> Result(loading = false, data = data, error = refreshStatus.error)
        }

    protected abstract fun loadFromNetwork(): Single<T>

    protected abstract fun loadFromDatabase(): Flowable<T>

    protected abstract fun saveToDatabase(value: T)

    private fun doRefresh(): Completable {
        return loadFromNetwork()
            .flatMapCompletable { Completable.fromAction { saveToDatabase(it) } }
            .doOnSubscribe { refreshStatuses.onNext(InProgress) }
            .doOnComplete { refreshStatuses.onNext(Idle) }
            .doOnError { refreshStatuses.onNext(Error(it)) }
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
    }
}

private object RefreshEvent

private sealed class RefreshStatus {
    object Idle : RefreshStatus()
    object InProgress : RefreshStatus()
    data class Error(val error: Throwable) : RefreshStatus()
}