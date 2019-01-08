package com.wtmberlin.data

import androidx.room.*
import org.threeten.bp.Duration
import org.threeten.bp.LocalDateTime

data class WtmGroup(
    val pastEventCount: Int,
    val members: Int
)

@Entity
data class WtmEvent(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "username") val name: String,
    @ColumnInfo(name = "date") val localDateTime: LocalDateTime,
    @ColumnInfo(name = "venue") val venueName: String?
)

@Entity
data class DetailedWtmEvent(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "username") val name: String,
    @ColumnInfo(name = "local_date_time_start") val localDateTimeStart: LocalDateTime,
    @ColumnInfo(name = "time_start") val timeStart: Long,
    @ColumnInfo(name = "duration") val duration: Duration,
    @ColumnInfo(name = "venue_name") val venueName: String?,
    @ColumnInfo(name = "venue_address") val venueAddress: String?,
    @Embedded(prefix = "venue_coordinates_") val venueCoordinates: Coordinates?,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "photo_url") val photoUrl: String?
) {
    @Ignore
    val localDateTimeEnd: LocalDateTime = localDateTimeStart.plus(duration)
}

data class Venue(val name: String = "Default Company")

data class Coordinates(
    @ColumnInfo(name = "latitude") val latitude: String,
    @ColumnInfo(name = "longitude") val longitude: String
)