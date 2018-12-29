package com.wtmberlin

import org.threeten.bp.LocalDateTime

sealed class EventsAdapterItem {
    data class EventItem(
        val id: String,
        val name: String,
        val localDateTime: LocalDateTime,
        val venueName: String?): EventsAdapterItem()
}