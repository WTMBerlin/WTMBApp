package com.wtmberlin.data

import com.wtmberlin.meetup.*
import io.reactivex.Flowable
import io.reactivex.Single
import org.threeten.bp.LocalDateTime

class Repository(private val apiService: MeetupService, private val database: Database) {
    fun venues(): Flowable<Result<List<Venue>>> {
        return apiService.events()
            .map { it.map(MeetupEvent::toWtmVenue) }
            .map { it.distinct() }
            .map { Result(loading = false, data = it, error = null) }
            .onErrorReturn { Result(loading = false, data = null, error = it) }.toFlowable()
    }

    fun group(): Flowable<Result<WtmGroup>> {
        return apiService.group()
            .map { it.toWtmGroup() }
            .map { Result(loading = false, data = it, error = null) }
            .onErrorReturn { Result(loading = false, data = null, error = it) }
            .toFlowable()
    }

    fun events(): Flowable<Result<List<WtmEvent>>> {
        return eventsResource.values()
    }

    fun refreshEvents() {
        eventsResource.refresh()
    }

    private val eventsResource = object : NetworkBoundResource<List<WtmEvent>, List<WtmEvent>>() {
        override fun loadFromNetwork(): Single<List<WtmEvent>> {
            return apiService.events().map { it.map(MeetupEvent::toWtmEvent) }
        }

        override fun loadFromDatabase(): Flowable<List<WtmEvent>> {
            return database.wtmEventDAO().getAll()
        }

        override fun saveToDatabase(value: List<WtmEvent>) {
            database.runInTransaction {
                database.wtmEventDAO().clear()
                database.wtmEventDAO().insertAll(value)
            }
        }
    }

    fun event(eventId: String): Flowable<Result<DetailedWtmEvent>> = DetailedEventResource(eventId).values()

    inner class DetailedEventResource(private val eventId: String) : NetworkBoundResource<DetailedWtmEvent, DetailedWtmEvent>() {
        override fun loadFromNetwork(): Single<DetailedWtmEvent> {
            return apiService.event(eventId).map { it.toDetailedWtmEvent() }
        }

        override fun loadFromDatabase(): Flowable<DetailedWtmEvent> {
            return database.detailedWtmEventDao().getById(eventId)
        }

        override fun saveToDatabase(value: DetailedWtmEvent) {
            database.detailedWtmEventDao().insert(value)
        }
    }
}

private fun MeetupEvent.toWtmEvent() = WtmEvent(
    id = id,
    name = name,
    localDateTime = LocalDateTime.of(local_date, local_time),
    venueName = venue?.name
)

private fun MeetupDetailedEvent.toDetailedWtmEvent() = DetailedWtmEvent(
    id = id,
    name = name,
    localDateTimeStart = LocalDateTime.of(local_date, local_time),
    timeStart = time,
    duration = duration,
    venueName = venue?.name,
    venueAddress = venue?.addressText(),
    venueCoordinates = venue?.let { Coordinates(latitude = it.lat, longitude = it.lon) },
    description = description,
    photoUrl = featured_photo?.photo_link
)

private fun MeetupDetailedVenue.addressText() =
    StringBuilder().apply {
        address_1?.let {
            append(it)
            append(" ")
        }

        address_2?.let {
            append(it)
            append(" ")
        }

        address_3?.let {
            append(it)
            append(" ")
        }

        city?.let {
            append(it)
        }
    }.toString()


private fun MeetupGroup.toWtmGroup() = WtmGroup(
    pastEventCount = past_event_count,
    members = members
)

private fun MeetupEvent.toWtmVenue() = Venue(
    name = venue?.name ?: ""
)
