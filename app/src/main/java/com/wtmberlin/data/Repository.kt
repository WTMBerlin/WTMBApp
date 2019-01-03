package com.wtmberlin.data

import androidx.room.*
import io.reactivex.Flowable
import org.threeten.bp.LocalDateTime

class Repository(private val apiService: MeetupService, private val database: EventDatabase) {
    fun events(): Flowable<Result<List<WtmEvent>>> {
        return Flowable.merge(eventsFromDatabase(), eventsFromNetwork())
            .map { Result.success(it) }
            .onErrorReturn { Result.error(it) }
    }

    fun venues(): Flowable<Result<List<Venue>>> {
        return apiService.events()
            .map { it.map(MeetupEvent::toWtmVenue) }
            .map { Result.success(it) }
            .onErrorReturn { Result.error(it) }.toFlowable()
    }

    private fun eventsFromNetwork() = apiService.events()
            .map { it.map(MeetupEvent::toWtmEvent) }
            .doOnSuccess { saveToDatabase(it) }
            .toFlowable()

    private fun saveToDatabase(events: List<WtmEvent>) {
        database.runInTransaction {
            database.wtmEventDAO().clear()
            database.wtmEventDAO().insertAll(events)
        }
    }

    private fun eventsFromDatabase() = database.wtmEventDAO().getAll()

    fun event(eventId: String): Flowable<Result<WtmEvent>> {
        return apiService.event(eventId)
            .map { it.toWtmEvent() }
            .map { Result.success(it) }
            .onErrorReturn { Result.error(it) }
            .toFlowable()
    }

    fun group(): Flowable<Result<WtmGroup>> {
        return apiService.group()
            .map { it.toWtmGroup() }
            .map { Result.success(it) }
            .onErrorReturn { Result.error(it) }
            .toFlowable()
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

@Dao
interface WtmEventDAO {
    @Query("SELECT * FROM WtmEvent")
    fun getAll(): Flowable<List<WtmEvent>>

    @Query("SELECT * FROM WtmEvent WHERE id IN (:wtmEventId)")
    fun loadById(wtmEventId: Int): Flowable<WtmEvent>

    @Query("DELETE FROM WtmEvent")
    fun clear()

    @Insert
    fun insertAll(wtmEvents: List<WtmEvent>)
}

private fun MeetupEvent.toWtmEvent() = WtmEvent(
    id = id,
    name = name,
    localDateTime = LocalDateTime.of(local_date, local_time),
    venueName = venue?.name
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
