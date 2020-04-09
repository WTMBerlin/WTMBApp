package com.wtmberlin.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface WtmEventDao {
    @Query("SELECT * FROM WtmEvent")
    suspend fun getAll(): List<WtmEvent>

    @Query("SELECT * FROM WtmEvent WHERE id = :eventId")
    suspend fun getById(eventId: String): WtmEvent?

    @Transaction
    fun replaceAll(newWtmEvents: List<WtmEvent>) {
        clear()
        insertAll(newWtmEvents)
    }

    @Query("DELETE FROM WtmEvent")
    fun clear()

    @Insert
    fun insertAll(wtmEvents: List<WtmEvent>)
}