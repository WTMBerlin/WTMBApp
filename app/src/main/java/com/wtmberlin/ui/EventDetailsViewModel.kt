package com.wtmberlin.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wtmberlin.SchedulerProvider
import com.wtmberlin.data.Coordinates
import com.wtmberlin.data.Repository
import com.wtmberlin.data.Result
import com.wtmberlin.data.WtmEvent
import com.wtmberlin.util.Event
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class EventDetailsViewModel(
    eventId: String,
    repository: Repository,
    schedulerProvider: SchedulerProvider
) : ViewModel() {
    val event = MutableLiveData<WtmEvent>()

    val addToCalendar = MutableLiveData<AddToCalendarEvent>()
    val openMaps = MutableLiveData<OpenMapsEvent>()
    val openMeetupPage = MutableLiveData<OpenMeetupPageEvent>()
    val shareEvent = MutableLiveData<String>()

    private val subscriptions = CompositeDisposable()

    init {
        subscriptions.add(
            repository.event(eventId)
                .subscribeOn(schedulerProvider.io)
                .observeOn(schedulerProvider.ui)
                .subscribe(this::onDataLoaded)
        )
    }

    private fun onDataLoaded(result: Result<WtmEvent>) {
        result.data?.let {
            event.value = it
        }
        result.error?.let { Timber.i(it) }
    }

    fun onDateTimeClicked() {
        event.value?.let {
            addToCalendar.value = it.toCalendarEvent()
        }
    }

    fun onLocationClicked() {
        event.value?.let {
            if (it.venue?.coordinates != null) {
                openMaps.value = OpenMapsEvent(it.venue.name, it.venue.coordinates)
            }
        }
    }

    fun onOpenMeetupPageClicked() {
        event.value?.let {
            openMeetupPage.value = OpenMeetupPageEvent(it.meetupUrl)
        }
    }

    fun onSharingMeetupClicked() {
        event.value?.let {
            shareEvent.value = it.meetupUrl
        }
    }

    override fun onCleared() {
        super.onCleared()

        subscriptions.clear()
    }
}

data class OpenMapsEvent(val venueName: String, val coordinates: Coordinates) : Event()

data class AddToCalendarEvent(
    val beginTime: Long,
    val endTime: Long,
    val title: String,
    val location: String?
) : Event()

data class OpenMeetupPageEvent(val url: String) : Event()

private fun WtmEvent.toCalendarEvent() = AddToCalendarEvent(
    beginTime = dateTimeStart.toInstant().toEpochMilli(),
    endTime = dateTimeStart.toInstant().toEpochMilli() + duration.toMillis(),
    title = name,
    location = venue?.name
)
