package com.wtmberlin.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wtmberlin.data.Coordinates
import com.wtmberlin.data.Repository
import com.wtmberlin.data.Result
import com.wtmberlin.data.WtmEvent
import com.wtmberlin.util.Event
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class EventDetailsViewModel(eventId: String, repository: Repository) : ViewModel() {
    val event = MutableLiveData<WtmEvent>()

    val addToCalendar = MutableLiveData<AddToCalendarEvent>()
    val openMaps = MutableLiveData<OpenMapsEvent>()

    private val subscriptions = CompositeDisposable()

    init {
        subscriptions.add(
            repository.event(eventId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onDataLoaded)
        )
    }

    private fun onDataLoaded(result: Result<WtmEvent>) {
        result.data?.let { event.value = it }
        result.error?.let { Timber.i(it) }
    }

    fun onDateTimeClicked() {
        event.value?.let {
            addToCalendar.value = AddToCalendarEvent(it.toCalendarEvent())
        }
    }

    fun onLocationClicked() {
        event.value?.let {
            if (it.venue?.coordinates != null) {
                openMaps.value = OpenMapsEvent(it.venue.name, it.venue.coordinates)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()

        subscriptions.clear()
    }
}

data class OpenMapsEvent(val venueName: String, val coordinates: Coordinates) : Event()

data class AddToCalendarEvent(val calendarEvent: CalendarEvent) : Event()

data class CalendarEvent(
    val beginTime: Long,
    val endTime: Long,
    val title: String,
    val location: String?
)

private fun WtmEvent.toCalendarEvent() = CalendarEvent(
    beginTime = dateTimeStart.toInstant().toEpochMilli(),
    endTime = dateTimeStart.toInstant().toEpochMilli() + duration.toMillis(),
    title = name,
    location = venue?.name
)