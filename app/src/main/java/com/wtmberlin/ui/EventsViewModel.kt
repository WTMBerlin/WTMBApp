package com.wtmberlin.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wtmberlin.data.Repository
import com.wtmberlin.data.Result
import com.wtmberlin.data.WtmEvent
import com.wtmberlin.util.Event
import kotlinx.coroutines.launch
import org.threeten.bp.ZonedDateTime
import timber.log.Timber

class EventsViewModel(private val repository: Repository) : ViewModel() {
    val adapterItems = MutableLiveData<List<EventsAdapterItem>>()
    val refreshing = MutableLiveData<Boolean>()
    val displayEventDetails = MutableLiveData<DisplayEventDetailsEvent>()

    init {
        viewModelScope.launch {
            onDataLoaded(repository.events())
        }
    }

    fun refreshEvents() {
        viewModelScope.launch {
            repository.refreshEvents()
            onDataLoaded(repository.events())
        }
    }

    fun onEventItemClicked(item: EventItem) {
        displayEventDetails.value = DisplayEventDetailsEvent(item.id)
    }

    private fun onDataLoaded(result: Result<List<WtmEvent>>) {
        refreshing.value = result.loading
        result.data?.let { processEvents(it) }
        result.error?.let { Timber.i(it) }
    }

    private fun processEvents(events: List<WtmEvent>) {
        val upcomingEvents = mutableListOf<WtmEvent>()
        val pastEvents = mutableListOf<WtmEvent>()
        val now = ZonedDateTime.now()

        for (event in events) {
            if (event.dateTimeStart.isAfter(now)) upcomingEvents += event else pastEvents += event
        }

        val adapterItems = mutableListOf<EventsAdapterItem>()

        if (upcomingEvents.isNotEmpty()) {
            adapterItems += UpcomingHeaderItem
            adapterItems += upcomingEvents.map { it.toEventItem() }
        } else {
            adapterItems += NoUpcomingEventsItem
        }

        if (pastEvents.isNotEmpty()) {
            adapterItems += PastHeaderItem
            adapterItems += pastEvents.map { it.toEventItem() }
        }

        this.adapterItems.value = adapterItems
    }

    private fun WtmEvent.toEventItem() = EventItem(
        id = id,
        name = name,
        localDateTime = dateTimeStart.toLocalDateTime(),
        venueName = venue?.name
    )

}

data class DisplayEventDetailsEvent(val eventId: String) : Event()