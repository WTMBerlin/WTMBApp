package com.wtmberlin.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Flowable

@Dao
interface WtmEventDao {
    @Query("SELECT * FROM WtmEvent")
    fun getAll(): Flowable<List<WtmEvent>>

    @Query("SELECT * FROM WtmEvent WHERE id = :eventId")
    fun getById(eventId: String): Flowable<WtmEvent>

    @Query("DELETE FROM WtmEvent")
    fun clear()

    @Insert
    fun insertAll(wtmEvents: List<WtmEvent>)
}