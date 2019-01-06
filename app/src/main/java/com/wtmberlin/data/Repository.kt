package com.wtmberlin.data

import androidx.room.*
import io.reactivex.Flowable
import io.reactivex.Single
import org.threeten.bp.Duration
import org.threeten.bp.LocalDateTime

class Repository(private val apiService: MeetupService, private val database: EventDatabase) {
    fun venues(): Flowable<Result<List<Venue>>> {
        return apiService.events()
            .map { it.map(MeetupEvent::toWtmVenue) }
            .map { Result.success(it) }
            .onErrorReturn { Result.error(it) }.toFlowable()
    }

    fun group(): Flowable<Result<WtmGroup>> {
        return apiService.group()
            .map { it.toWtmGroup() }
            .map { Result.success(it) }
            .onErrorReturn { Result.error(it) }
            .toFlowable()
    }

    fun events(): Flowable<BetterResult<List<WtmEvent>>> {
        return eventsResource.values()
    }

    fun refreshEvents() {
        eventsResource.refresh()
    }

    private val eventsResource = object : NetworkBoundResource<List<WtmEvent>>() {
        override fun loadFromNetwork(): Single<List<WtmEvent>> {
            return apiService.events().map { it.map(MeetupEvent::toWtmEvent) }
        }

        override fun loadFromDatabase(): Flowable<List<WtmEvent>> {
            return database.wtmEventDAO().getAll()
        }

        override fun saveToDatabase(value: List<WtmEvent>) {
            database.runInTransaction {
                database.wtmEventDAO().clear()
                database.wtmEventDAO().insertAll(value)
            }
        }
    }

    fun event(eventId: String): Flowable<BetterResult<DetailedWtmEvent>> = DetailedEventResource(eventId).values()

    inner class DetailedEventResource(private val eventId: String): NetworkBoundResource<DetailedWtmEvent>() {
        override fun loadFromNetwork(): Single<DetailedWtmEvent> {
            return apiService.event(eventId).map { it.toDetailedWtmEvent() }
        }

        override fun loadFromDatabase(): Flowable<DetailedWtmEvent> {
            return database.detailedWtmEventDao().getById(eventId)
        }

        override fun saveToDatabase(value: DetailedWtmEvent) {
            database.detailedWtmEventDao().insert(value)
        }
    }
}

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

@Dao
interface WtmEventDAO {
    @Query("SELECT * FROM WtmEvent")
    fun getAll(): Flowable<List<WtmEvent>>

    @Query("DELETE FROM WtmEvent")
    fun clear()

    @Insert
    fun insertAll(wtmEvents: List<WtmEvent>)
}

@Dao
interface DetailedWtmEventDAO {
    @Query("SELECT * FROM DetailedWtmEvent WHERE id = :eventId")
    fun getById(eventId: String): Flowable<DetailedWtmEvent>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(detailedWtmEvent: DetailedWtmEvent)
}

private fun MeetupEvent.toWtmEvent() = WtmEvent(
    id = id,
    name = name,
    localDateTime = LocalDateTime.of(local_date, local_time),
    venueName = venue?.name
)

private fun MeetupDetailedEvent.toDetailedWtmEvent() = DetailedWtmEvent(
    id = id,
    name = name,
    localDateTimeStart = LocalDateTime.of(local_date, local_time),
    timeStart = time,
    duration = duration,
    venueName = venue?.name,
    venueAddress = venue?.let { "${it.address_1} ${it.address_2} · ${it.city}" },
    venueCoordinates = venue?.let { Coordinates(latitude = it.lat, longitude = it.lon) },
    description = description,
    photoUrl = featured_photo?.photo_link
)

private fun MeetupGroup.toWtmGroup() = WtmGroup(
    pastEventCount = past_event_count,
    members = members
)

private fun MeetupEvent.toWtmVenue() = Venue(
    id = id,
    name = venue?.name ?: ""
)

data class Venue(val id: String, val name: String = "Default Company")

data class Coordinates(
    @ColumnInfo(name = "latitude") val latitude: String,
    @ColumnInfo(name = "longitude") val longitude: String)
