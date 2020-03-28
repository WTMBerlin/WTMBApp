package com.wtmberlin.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import org.threeten.bp.Duration
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

@TypeConverters(ZonedDateTimeConverter::class, DurationConverter::class)
@Database(entities = [WtmEvent::class], version = 5)
abstract class Database : RoomDatabase() {
    abstract fun wtmEventDAO(): WtmEventDao
}

class ZonedDateTimeConverter {
    @TypeConverter
    fun save(date: ZonedDateTime): String = date.format(DateTimeFormatter.ISO_ZONED_DATE_TIME)

    @TypeConverter
    fun load(date: String): ZonedDateTime = ZonedDateTime.parse(date, DateTimeFormatter.ISO_ZONED_DATE_TIME)
}

class DurationConverter {
    @TypeConverter
    fun save(duration: Duration?): Long = duration?.toMillis() ?: 0L

    @TypeConverter
    fun load(duration: Long?): Duration =
        if (duration != null) Duration.ofMillis(duration) else Duration.ofMillis(0)
}

