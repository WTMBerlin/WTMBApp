package com.wtmberlin.data

import com.wtmberlin.util.CoroutinesDispatcherProvider
import kotlinx.coroutines.withContext

open class Repository(
    private val apiService: ApiService,
    private val database: Database,
    private val dispatchers: CoroutinesDispatcherProvider
) {
    suspend fun venues() = withContext(dispatchers.io) {
        com.wtmberlin.data.runCatching {
            Result(
                loading = true, data = apiService.events()
                    .map(::toVenueName)
                    .distinct()
                    .sortedBy { it.name.toLowerCase().trimStart() }, error = null
            )
        }
    }

    suspend fun events() = withContext(dispatchers.io) {
        fetchEvents()
    }

    suspend fun events2017() = withContext(dispatchers.io) {
        com.wtmberlin.data.runCatching {
            Result(loading = false, data = apiService.events2017(), error = null)
        }
    }

    suspend fun events2018() = withContext(dispatchers.io) {
        com.wtmberlin.data.runCatching {
            Result(loading = false, data = apiService.events2018(), error = null)
        }
    }

    suspend fun events2019() = withContext(dispatchers.io) {
        com.wtmberlin.data.runCatching {
            Result(loading = false, data = apiService.events2019(), error = null)
        }
    }

    suspend fun eventsTotal() = withContext(dispatchers.io) {
        com.wtmberlin.data.runCatching {
            Result(loading = false, data = apiService.events2019(), error = null)
        }
    }

    suspend fun members() = withContext(dispatchers.io) {
        com.wtmberlin.data.runCatching {
            Result(loading = false, data = apiService.members(), error = null)
        }
    }

    suspend fun refreshEvents() = withContext(dispatchers.io) {
        fetchEvents()
    }


    private suspend fun fetchEvents() = withContext(dispatchers.io) {
        com.wtmberlin.data.runCatching {
            Result(loading = false, data = fetchEventsFromDb(), error = null)
        }
    }

    private suspend fun updateDb() = withContext(dispatchers.io) {
        database.wtmEventDAO().replaceAll(apiService.events())
    }

    private suspend fun fetchEventsFromDb() = withContext(dispatchers.io) {
        database.wtmEventDAO().getAll()
    }

    open suspend fun event(eventId: String) = withContext(dispatchers.io) {
        Result(loading = true, data = database.wtmEventDAO().getById(eventId), error = null)
    }

    private fun toVenueName(event: WtmEvent) = VenueName(name = event.venue?.name ?: "")
}

data class VenueName(val name: String = "")
