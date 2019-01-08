package com.wtmberlin.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import org.threeten.bp.Duration
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

@TypeConverters(LocalDateTimeConverter::class, DurationConverter::class)
@Database(entities = [WtmEvent::class, DetailedWtmEvent::class], version = 2)
abstract class EventDatabase : RoomDatabase() {
    abstract fun wtmEventDAO(): WtmEventDAO

    abstract fun detailedWtmEventDao(): DetailedWtmEventDAO
}

class LocalDateTimeConverter {
    @TypeConverter
    fun save(date: LocalDateTime): String = date.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)

    @TypeConverter
    fun load(date: String): LocalDateTime = LocalDateTime.parse(date, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
}

class DurationConverter {
    @TypeConverter
    fun save(duration: Duration): Long = duration.toMillis()

    @TypeConverter
    fun load(duration: Long): Duration = Duration.ofMillis(duration)
}