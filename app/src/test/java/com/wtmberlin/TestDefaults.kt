package com.wtmberlin

import com.wtmberlin.data.Coordinates
import com.wtmberlin.data.Venue
import com.wtmberlin.data.WtmEvent
import org.threeten.bp.Duration
import org.threeten.bp.ZonedDateTime

/**
 * Functions to create test objects initialized with sensible defaults.
 */

fun defaultWtmEvent(
    id: String = "1",
    name: String = "Event Name",
    dateTimeStart: ZonedDateTime = ZonedDateTime.now(),
    duration: Duration = Duration.ofHours(3),
    description: String = "Event Description",
    photoUrl: String? = "https://wtmberlin.com/events/photos/1",
    meetupUrl: String = "https://www.meetup.com/event/1",
    venue: Venue = defaultVenue()
) = WtmEvent(id, name, dateTimeStart, duration, description, photoUrl, meetupUrl, venue)

fun defaultVenue(
    name: String = "Venue Name",
    address: String? = "Venue Address",
    coordinates: Coordinates? = defaultCoordinates()
) = Venue(name, address, coordinates)

fun defaultCoordinates(
    latitude: Double = 12.34,
    longitude: Double = 56.78
) = Coordinates(latitude, longitude)