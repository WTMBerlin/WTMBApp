package com.wtmberlin.data

import io.reactivex.Single
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import retrofit2.http.GET

interface MeetupService {
    @GET("Women-Techmakers-Berlin/events")
    fun events(): Single<List<MeetupEvent>>
}

data class MeetupEvent(
    val id: String,
    val name: String,
    val local_date: LocalDate,
    val local_time: LocalTime,
    val venue: MeetupVenue?
)

data class MeetupVenue(
    val name: String)