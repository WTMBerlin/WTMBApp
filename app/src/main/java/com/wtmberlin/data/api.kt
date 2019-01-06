package com.wtmberlin.data

import io.reactivex.Single
import org.threeten.bp.Duration
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MeetupService {
    @GET("Women-Techmakers-Berlin/events?status=cancelled,past,upcoming&desc=true&only=id,name,local_date,local_time,venue.name&page=30")
    fun events(): Single<List<MeetupEvent>>

    @GET("Women-Techmakers-Berlin/events/{eventId}?only=id,name,time,local_date,local_time,duration,venue,description,featured_photo.photo_link&fields=featured_photo")
    fun event(@Path("eventId") eventId: String): Single<MeetupDetailedEvent>

    @GET("Women-Techmakers-Berlin?only=past_event_count,members")
    fun group():Single<MeetupGroup>
}

data class MeetupGroup(
    val past_event_count: Int,
    val members: Int)

data class MeetupEvent(
    val id: String,
    val name: String,
    val local_date: LocalDate,
    val local_time: LocalTime,
    val venue: MeetupVenue?)

data class MeetupDetailedEvent(
    val id: String,
    val name: String,
    val description: String,
    val featured_photo: MeetupPhoto?,
    val time: Long,
    val local_date: LocalDate,
    val local_time: LocalTime,
    val duration: Duration,
    val venue: MeetupDetailedVenue?)

data class MeetupVenue(
    val id: String,
    val name: String)

data class MeetupDetailedVenue(
    val id: String,
    val name: String,
    val address_1: String,
    val address_2: String,
    val address_3: String,
    val city: String,
    val lat: String,
    val lon: String)

data class MeetupPhoto(
    val photo_link: String)
