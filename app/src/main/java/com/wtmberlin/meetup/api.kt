package com.wtmberlin.meetup

import io.reactivex.Single
import retrofit2.http.GET

interface MeetupService {
    @GET("Women-Techmakers-Berlin/events?status=cancelled,past,upcoming&desc=true&only=id,name,time,utc_offset,duration,venue,description,featured_photo.photo_link,link&fields=featured_photo&omit=venue.localized_country_name,venue.repinned,venue.phone&page=200")
    fun events(): Single<List<MeetupEvent>>
}

data class MeetupEvent(
    val id: String,
    val name: String,
    val description: String,
    val featured_photo: MeetupPhoto?,
    val link: String,
    val time: Long,
    val utc_offset: Long,
    val duration: Long,
    val venue: MeetupVenue?
)

data class MeetupVenue(
    val id: String,
    val name: String,
    val address_1: String?,
    val address_2: String?,
    val address_3: String?,
    val city: String?,
    val lat: Double?,
    val lon: Double?
)

data class MeetupPhoto(
    val photo_link: String
)
