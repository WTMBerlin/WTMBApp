package com.wtmberlin.data

import com.wtmberlin.data.RefreshStatus.*
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.processors.BehaviorProcessor
import io.reactivex.processors.PublishProcessor
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

abstract class NetworkBoundResource<NetworkT, DatabaseT> {
    private val refreshEvents = PublishProcessor.create<RefreshEvent>()

    private val refreshStatuses = BehaviorProcessor.create<RefreshStatus>()
    private var refreshStart = 0L

    init {
        refreshEvents
            .doOnNext { refreshStart = System.currentTimeMillis() }
            .flatMapCompletable {
                doRefresh()
                    .doOnComplete { Timber.w("Refresh time: ${System.currentTimeMillis() - refreshStart}") }
            }
            .subscribe()

        refreshStatuses.onNext(Idle)
    }

    fun refresh() {
        refreshEvents.onNext(RefreshEvent)
    }

    fun values(): Flowable<Result<DatabaseT>> {
        return Flowable.combineLatest(
            loadFromDatabase(),
            refreshStatuses,
            BiFunction { data: DatabaseT, refreshStatus: RefreshStatus -> toResult(data, refreshStatus) }
        )
            .onErrorReturn { Result<DatabaseT>(loading = false, data = null, error = it) }
    }

    private fun toResult(data: DatabaseT, refreshStatus: RefreshStatus) =
        when (refreshStatus) {
            is Idle -> Result(loading = false, data = data, error = null)
            is InProgress -> Result(loading = true, data = data, error = null)
            is Error -> Result(loading = false, data = data, error = refreshStatus.error)
        }

    protected abstract fun loadFromNetwork(): Single<NetworkT>

    protected abstract fun loadFromDatabase(): Flowable<DatabaseT>

    protected abstract fun saveToDatabase(value: NetworkT)

    private fun doRefresh(): Completable {
        return loadFromNetwork()
            .flatMapCompletable { Completable.fromAction { saveToDatabase(it) } }
            .doOnSubscribe { refreshStatuses.onNext(InProgress) }
            .doOnComplete { refreshStatuses.onNext(Idle) }
            .doOnError { refreshStatuses.onNext(Error(it)) }
            .onErrorComplete()
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