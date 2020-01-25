package com.wtmberlin.data

import com.wtmberlin.meetup.MeetupMembers
import io.reactivex.Flowable
import io.reactivex.Single

open class Repository(private val apiService: ApiService, private val database: Database) {
    fun venues(): Flowable<Result<List<VenueName>>> {
        return apiService.events()
            .map { it.map(WtmEvent::toVenueName) }
            .map { it.distinct() }
            .map { it.sortedBy { vName -> vName.name.toLowerCase().trimStart() } }
            .map { Result(loading = false, data = it, error = null) }
            .onErrorReturn { Result(loading = false, data = null, error = it) }.toFlowable()
    }


    open fun events(): Flowable<Result<List<WtmEvent>>> {
        return eventsResource.values()
            .map {
                if (it.data != null && it.data.isEmpty()) {
                    Result(it.loading, null, it.error)
                } else {
                    it
                }
            }
            .doOnSubscribe { eventsResource.refresh() }
    }

    open fun events2017(): Flowable<Result<List<WtmEvent>>> {
        return apiService.events2017()
            .map { Result(loading = false, data = it, error = null) }
            .onErrorReturn { Result(loading = false, data = null, error = it) }.toFlowable()
    }
    open fun events2018(): Flowable<Result<List<WtmEvent>>> {
        return apiService.events2018()
            .map { Result(loading = false, data = it, error = null) }
            .onErrorReturn { Result(loading = false, data = null, error = it) }.toFlowable()
    }
    open fun events2019(): Flowable<Result<List<WtmEvent>>> {
        return apiService.events2019()
            .map { Result(loading = false, data = it, error = null) }
            .onErrorReturn { Result(loading = false, data = null, error = it) }.toFlowable()
    }

    open fun events2020(): Flowable<Result<List<WtmEvent>>> {
        return apiService.events2020()
            .map { Result(loading = false, data = it, error = null) }
            .onErrorReturn { Result(loading = false, data = null, error = it) }.toFlowable()
    }
    open fun eventsTotal(): Flowable<Result<List<WtmEvent>>> {
        return apiService.events2019()
            .map { Result(loading = false, data = it, error = null) }
            .onErrorReturn { Result(loading = false, data = null, error = it) }.toFlowable()
    }
    open fun members(): Flowable<Result<MeetupMembers>> {
        return apiService.members()
            .map { Result(loading = false, data = it, error = null) }
            .onErrorReturn { Result(loading = false, data = null, error = it) }.toFlowable()
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
