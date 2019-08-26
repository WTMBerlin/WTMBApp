package com.wtmberlin.ui

import androidx.lifecycle.MutableLiveData
import com.wtmberlin.data.Coordinates
import com.wtmberlin.data.Repository
import com.wtmberlin.data.Result
import com.wtmberlin.data.WtmEvent
import com.wtmberlin.util.CoroutineViewModel
import com.wtmberlin.util.Event
import org.threeten.bp.Duration
import com.wtmberlin.util.LogException
import kotlinx.coroutines.launch

class EventDetailsViewModel(
    eventId: String,
    repository: Repository,
    private val logException: LogException
) : CoroutineViewModel() {
    val event = MutableLiveData<WtmEvent>()

    val addToCalendar = MutableLiveData<AddToCalendarEvent>()
    val openMaps = MutableLiveData<OpenMapsEvent>()
    val openMeetupPage = MutableLiveData<OpenMeetupPageEvent>()
    private val shareEvent = MutableLiveData<String>()

    init {
        launch {
            val eventsById = repository.event(eventId)
            onDataLoaded(eventsById)
        }
    }

    private fun onDataLoaded(result: Result<WtmEvent>) {
        result.data?.let { event.value = it }
        result.error?.let { logException.getException(it) }
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

}

data class OpenMapsEvent(val venueName: String, val coordinates: Coordinates) : Event()

data class AddToCalendarEvent(
    val beginTime: Long,
    val endTime: Long,
    val title: String,
    val location: String?
) : Event()

data class OpenMeetupPageEvent(val url: String) : Event()

private fun WtmEvent.toCalendarEvent() =
    AddToCalendarEvent(
        beginTime = dateTimeStart.toInstant().toEpochMilli(),
        endTime = dateTimeStart.toInstant()
            .toEpochMilli() + ensureDurationNonNull(duration).toMillis(),
        title = name,
        location = venue?.name
    )

fun ensureDurationNonNull(duration: Duration?): Duration = duration ?: Duration.ofMillis(0)
