package com.wtmberlin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wtmberlin.data.BetterResult
import com.wtmberlin.data.Repository
import com.wtmberlin.data.WtmEvent
import com.wtmberlin.util.Event
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.threeten.bp.LocalDateTime
import timber.log.Timber

class EventsViewModel(private val repository: Repository) : ViewModel() {
    val adapterItems = MutableLiveData<List<EventsAdapterItem>>()
    val refreshing = MutableLiveData<Boolean>()
    val displayEventDetails = MutableLiveData<DisplayEventDetailsEvent>()

    private val subscriptions = CompositeDisposable()

    init {
        subscriptions.add(
            repository.events()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onDataLoaded)
        )
    }

    fun refreshEvents() {
        repository.refreshEvents()
    }

    fun onEventItemClicked(item: EventItem) {
        displayEventDetails.value = DisplayEventDetailsEvent(item.id)
    }

    private fun onDataLoaded(result: BetterResult<List<WtmEvent>>) {
        refreshing.value = result.loading
        result.data?.let { processEvents(it) }
        result.error?.let { Timber.i(it) }
    }

    private fun processEvents(events: List<WtmEvent>) {
        val upcomingEvents = mutableListOf<WtmEvent>()
        val pastEvents = mutableListOf<WtmEvent>()
        val now = LocalDateTime.now()

        for (event in events) {
            if (event.localDateTime.isAfter(now)) upcomingEvents += event else pastEvents += event
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
        localDateTime = localDateTime,
        venueName = venueName
    )

    override fun onCleared() {
        super.onCleared()

        subscriptions.clear()
    }
}

data class DisplayEventDetailsEvent(val eventId: String) : Event()