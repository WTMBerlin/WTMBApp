package com.wtmberlin.data

import com.wtmberlin.meetup.MeetupMembers
import io.reactivex.Flowable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

open class Repository(private val apiService: ApiService, private val database: Database) {
    suspend fun venues() = withContext(Dispatchers.IO) {
        try {
            Result(
                loading = true, data = apiService.events()
                    .map(::toVenueName)
                    .distinct()
                    .sortedBy { it.name.toLowerCase().trimStart() }, error = null
            )
        } catch (e: Exception) {
            Result(loading = false, data = null, error = e)
        }
    }

    suspend fun events() = withContext(Dispatchers.IO) {
        fetchEvents()
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

    suspend fun refreshEvents() = withContext(Dispatchers.IO) {
        fetchEvents()
    }

    private suspend fun fetchEvents(): Result<List<WtmEvent>> {
        return try {
            updateDb()
            val fetchEvents = fetchEventsFromDb()
            Result(false, fetchEvents, null)
        } catch (e: Exception) {
            Result(false, null, e)
        }
    }

    private suspend fun updateDb() = withContext(Dispatchers.IO) {
        database.wtmEventDAO().replaceAll(apiService.events())
    }

    private suspend fun fetchEventsFromDb() = withContext(Dispatchers.IO) {
        database.wtmEventDAO().getAll()
    }

    open suspend fun event(eventId: String) = withContext(Dispatchers.IO) {
        Result(loading = true, data = database.wtmEventDAO().getById(eventId), error = null)
    }

    private fun toVenueName(event: WtmEvent) = VenueName(
        name = event.venue?.name ?: ""
    )
}

data class VenueName(val name: String = "")
