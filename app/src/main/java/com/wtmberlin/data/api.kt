package com.wtmberlin.data

import io.reactivex.Single
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

var accessToken: String? = null

interface MeetupService {
    @GET("Women-Techmakers-Berlin/events?status=cancelled,past,upcoming&desc=true&only=id,name,local_date,local_time,venue.name")
    fun events(): Single<List<MeetupEvent>>

    @GET("Women-Techmakers-Berlin/events/{eventId}")
    fun event(@Path("eventId") eventId: String): Single<MeetupEvent>

    @GET("Women-Techmakers-Berlin?only=past_event_count,members")
    fun group():Single<MeetupGroup>
}

data class MeetupGroup(
    val past_event_count: Int,
    val members: Int)

interface SecureMeetupService {
    @POST("oauth2/access")
    fun authorize(
        @Query("code") code: String,
        @Query("client_id") clientId: String = "8h22npmn9nfg58mco97blumdg9",
        @Query("client_secret") clientSecret: String = "u4668dtod20bpujdvmfqqd63sm",
        @Query("grant_type") grantType: String = "authorization_code",
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

data class TokenResponse(
    val access_token: String)
