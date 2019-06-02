package com.wtmberlin.data

import com.wtmberlin.meetup.MeetupEvent
import com.wtmberlin.meetup.MeetupService
import com.wtmberlin.meetup.MeetupVenue
import io.reactivex.Single
import org.threeten.bp.Duration
import org.threeten.bp.Instant
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime

open class ApiService(private val meetupService: MeetupService) {
    fun events(): Single<List<WtmEvent>> {
        return meetupService.events().map { it.map(MeetupEvent::toWtmEvent) }
    }
}


private fun MeetupEvent.toWtmEvent() = WtmEvent(
    id = id,
    name = name,
    dateTimeStart = ZonedDateTime.ofInstant(
        Instant.ofEpochMilli(time),
        ZoneOffset.ofTotalSeconds((utc_offset / 1000).toInt())
    ),
    duration = Duration.ofMillis(duration),
    description = description,
    photoUrl = featured_photo?.photo_link,
    meetupUrl = link,
    venue = venue?.let { venue ->
        Venue(
            name = venue.name,
            address = venue.addressText(),
            coordinates = venue.lat?.let {
                Coordinates(
                    latitude = venue.lat,
                    longitude = venue.lon!!
                )
            })
    }
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
