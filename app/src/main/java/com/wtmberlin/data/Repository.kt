package com.wtmberlin.data

import io.reactivex.Flowable
import io.reactivex.Single

open class Repository(private val apiService: ApiService, private val database: Database) {
    fun venues(): Flowable<Result<List<VenueName>>> {
        return apiService.events()
            .map { it.map(WtmEvent::toVenueName) }
            .map { it.distinct() }
            .map { Result(loading = false, data = it, error = null) }
            .onErrorReturn { Result(loading = false, data = null, error = it) }.toFlowable()
    }


    open fun events(): Flowable<Result<List<WtmEvent>>> {
        return eventsResource.values()
            .doOnSubscribe { eventsResource.refresh() }
    }

    fun refreshEvents() {
        eventsResource.refresh()
    }

    private val eventsResource = object : NetworkBoundResource<List<WtmEvent>, List<WtmEvent>>() {
        override fun loadFromNetwork(): Single<List<WtmEvent>> {
            return apiService.events()
        }

        override fun loadFromDatabase(): Flowable<List<WtmEvent>> {
            return database.wtmEventDAO().getAll()
        }

        override fun saveToDatabase(value: List<WtmEvent>) {
            database.wtmEventDAO().replaceAll(value)
        }
    }

    open fun event(eventId: String): Flowable<Result<WtmEvent>> = DetailedEventResource(eventId).values()

    inner class DetailedEventResource(private val eventId: String) : NetworkBoundResource<List<WtmEvent>, WtmEvent>() {
        override fun loadFromNetwork(): Single<List<WtmEvent>> {
            return apiService.events()
        }

        override fun loadFromDatabase(): Flowable<WtmEvent> {
            return database.wtmEventDAO().getById(eventId)
        }

        override fun saveToDatabase(value: List<WtmEvent>) {
            database.wtmEventDAO().replaceAll(value)
        }
    }
}

private fun WtmEvent.toVenueName() = VenueName(
    name = venue?.name ?: ""
)

data class VenueName(val name: String = "")