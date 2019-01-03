package com.wtmberlin.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

@TypeConverters(LocalDateTimeConverter::class)
@Database(entities = [WtmEvent::class], version = 1)
abstract class EventDatabase : RoomDatabase() {
    abstract fun wtmEventDAO(): WtmEventDAO
}

class LocalDateTimeConverter {
    @TypeConverter
    fun saveLocalDateTime(date: LocalDateTime) = date.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)


    @TypeConverter
    fun loadLocalDateTime(date: String) = LocalDateTime.parse(date, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
}