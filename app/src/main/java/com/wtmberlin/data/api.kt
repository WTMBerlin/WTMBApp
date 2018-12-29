package com.wtmberlin.data

import io.reactivex.Single
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import retrofit2.http.GET
import retrofit2.http.Query

var accessToken: String? = null

interface MeetupService {
    @GET("Women-Techmakers-Berlin/events")
    fun events(): Single<List<MeetupEvent>>
}

interface SecureMeetupService {
    @GET("oauth2/authorize")
    fun authorize(
        @Query("client_id") clientId: String = "8h22npmn9nfg58mco97blumdg9",
        @Query("response_type") responseType: String = "token",
        @Query("redirect_uri") redirectUrl: String = "http://wtmberlin.com/android-app"): Single<TokenResponse>
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

class TokenResponse {}