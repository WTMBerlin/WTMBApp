package com.wtmberlin.data

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import org.threeten.bp.Duration
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter

class LocalDateAdapter {
    @ToJson
    fun toJson(localDate: LocalDate): String {
        return localDate.format(DateTimeFormatter.ISO_LOCAL_DATE)
    }

    @FromJson
    fun fromJson(json: String): LocalDate {
        return LocalDate.parse(json, DateTimeFormatter.ISO_LOCAL_DATE)
    }
}

class LocalTimeAdapter {
    @ToJson
    fun toJson(localTime: LocalTime): String {
        return localTime.format(DateTimeFormatter.ISO_LOCAL_TIME)
    }

    @FromJson
    fun fromJson(json: String): LocalTime {
        return LocalTime.parse(json, DateTimeFormatter.ISO_LOCAL_TIME)
    }
}

class DurationAdapter {
    @ToJson
    fun toJson(duration: Duration): Long {
        return duration.toMillis()
    }

    @FromJson
    fun fromJson(json: Long): Duration {
        return Duration.ofMillis(json)
    }
}