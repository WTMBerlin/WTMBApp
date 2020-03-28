package com.wtmberlin.data

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.threeten.bp.Duration
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZonedDateTime

@Entity
data class WtmEvent(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "time") val dateTimeStart: ZonedDateTime,
    @ColumnInfo(name = "duration") val duration: Duration?,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "photo_url") val photoUrl: String?,
    @ColumnInfo(name = "meetup_url") val meetupUrl: String,
    @Embedded(prefix = "venue_") val venue: Venue?
) {
    fun localDateTimeStart(): LocalDateTime = dateTimeStart.toLocalDateTime()

    fun localDateTimeEnd(): LocalDateTime = if (duration != null) {
        dateTimeStart.plus(duration).toLocalDateTime()
    } else dateTimeStart.toLocalDateTime()
}

data class Venue(
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "address") val address: String?,
    @Embedded(prefix = "coordinates_") val coordinates: Coordinates?
)

data class Coordinates(
    @ColumnInfo(name = "latitude") val latitude: Double,
    @ColumnInfo(name = "longitude") val longitude: Double
)
