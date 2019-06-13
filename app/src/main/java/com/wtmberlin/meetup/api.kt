package com.wtmberlin.meetup

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface MeetupService {

    @GET("Women-Techmakers-Berlin/events")
    fun events(
        @Query("status") status: String = "cancelled,past,upcoming",
        @Query("desc") desc: String = "true",
        @Query("only") only: String = "id,name,time,utc_offset,duration,venue,description,featured_photo.photo_link,link",
        @Query("fields") fields: String = "featured_photo",
        @Query("omit") omit: String = "venue.localized_country_name,venue.repinned,venue.phone",
        @Query("page") page: String = "200"
    ): Single<List<MeetupEvent>>

    @GET("Women-Techmakers-Berlin/events")
    fun events2018(
        @Query("status") status: String = "cancelled,past,upcoming",
        @Query("desc") desc: String = "true",
        @Query("time") time: String = ",",
        @Query("only") only: String = "id,name,time,utc_offset,duration,venue,description,featured_photo.photo_link,link",
        @Query("fields") fields: String = "featured_photo",
        @Query("omit") omit: String = "venue.localized_country_name,venue.repinned,venue.phone",
        @Query("page") page: String = "200"
    ): Single<List<MeetupEvent>>

    @GET("Women-Techmakers-Berlin?only=past_event_count,members")
    fun group(): Single<MeetupGroup>
}

data class MeetupGroup(
    val past_event_count: Int,
    val members: Int
)

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
