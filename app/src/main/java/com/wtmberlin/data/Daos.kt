package com.wtmberlin.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Flowable

@Dao
interface WtmEventDao {
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