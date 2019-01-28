package com.wtmberlin.data

import com.wtmberlin.meetup.MeetupEvent
import com.wtmberlin.meetup.MeetupGroup
import com.wtmberlin.meetup.MeetupService
import com.wtmberlin.meetup.MeetupVenue
import io.reactivex.Flowable
import io.reactivex.Single
import org.threeten.bp.Duration
import org.threeten.bp.Instant
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime

open class Repository(private val apiService: MeetupService, private val database: Database) {
    fun venues(): Flowable<Result<List<VenueName>>> {
        return apiService.events()
            .map { it.map(MeetupEvent::toVenueName) }
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

    open fun events(): Flowable<Result<List<WtmEvent>>> {
        return eventsResource.values()
            .doOnSubscribe { eventsResource.refresh() }
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

    fun event(eventId: String): Flowable<Result<WtmEvent>> = DetailedEventResource(eventId).values()

    inner class DetailedEventResource(private val eventId: String) : NetworkBoundResource<List<WtmEvent>, WtmEvent>() {
        override fun loadFromNetwork(): Single<List<WtmEvent>> {
            return apiService.events().map { it.map(MeetupEvent::toWtmEvent) }
        }

        override fun loadFromDatabase(): Flowable<WtmEvent> {
            return database.wtmEventDAO().getById(eventId)
        }

        override fun saveToDatabase(value: List<WtmEvent>) {
            database.runInTransaction {
                database.wtmEventDAO().clear()
                database.wtmEventDAO().insertAll(value)
            }
        }
    }
}

private fun MeetupEvent.toWtmEvent() = WtmEvent(
    id = id,
    name = name,
    dateTimeStart = ZonedDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneOffset.ofTotalSeconds((utc_offset / 1000).toInt())),
    duration = Duration.ofMillis(duration),
    description = description,
    photoUrl = featured_photo?.photo_link,
    venue = venue?.let { venue -> Venue(
        name = venue.name,
        address = venue.addressText(),
        coordinates = venue.lat?.let { Coordinates(
            latitude = venue.lat,
            longitude = venue.lon!!)
        })}

)

private fun MeetupVenue.addressText() =
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

private fun MeetupEvent.toVenueName() = VenueName(
    name = venue?.name ?: ""
)

data class VenueName(val name: String = "")