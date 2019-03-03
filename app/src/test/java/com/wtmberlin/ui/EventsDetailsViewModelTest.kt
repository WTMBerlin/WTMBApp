package com.wtmberlin.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.wtmberlin.*
import com.wtmberlin.data.Coordinates
import com.wtmberlin.data.Repository
import com.wtmberlin.data.Result
import com.wtmberlin.data.WtmEvent
import io.reactivex.processors.PublishProcessor
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.`when`
import org.threeten.bp.Duration
import org.threeten.bp.ZonedDateTime

class EventsDetailsViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    val mockEvent = PublishProcessor.create<Result<WtmEvent>>()
    val mockRepository = mock<Repository>().also {
        `when`(it.event("1")).thenReturn(mockEvent)
    }

    val viewModel = EventDetailsViewModel("1", mockRepository, TestSchedulerProvider())

    @Test
    fun `emits event with given id`() {
        val observer = viewModel.event.testObserver()

        val event = defaultWtmEvent(id = "1")

        mockEvent.onNext(Result(loading = false, data = event, error = null))

        assertThat(observer.observedValues).containsExactly(event)
    }

    @Test
    fun `when datetime clicked emits add to calendar event`() {
        val observer = viewModel.addToCalendar.testObserver()

        val dateTimeStart = ZonedDateTime.now()
        val duration = Duration.ofHours(3)
        val dateTimeEnd = dateTimeStart.plus(duration)

        val event = defaultWtmEvent(
            id = "1",
            name = "Fun with Kotlin",
            dateTimeStart = dateTimeStart,
            duration = duration,
            venue = defaultVenue(name = "Google Berlin")
        )

        mockEvent.onNext(Result(loading = false, data = event, error = null))

        viewModel.onDateTimeClicked()

        assertThat(observer.observedValues).containsExactly(
            AddToCalendarEvent(
                title = "Fun with Kotlin",
                location = "Google Berlin",
                beginTime = dateTimeStart.toInstant().toEpochMilli(),
                endTime = dateTimeEnd.toInstant().toEpochMilli()
            )
        )
    }

    @Test
    fun `when location clicked emits open map event`() {
        val observer = viewModel.openMaps.testObserver()

        val event = defaultWtmEvent(
            id = "1",
            venue = defaultVenue(name = "Google Berlin", coordinates = Coordinates(12.34, 56.78))
        )

        mockEvent.onNext(Result(loading = false, data = event, error = null))

        viewModel.onLocationClicked()

        assertThat(observer.observedValues).containsExactly(
            OpenMapsEvent(
                venueName = "Google Berlin",
                coordinates = Coordinates(12.34, 56.78)
            )
        )
    }

    @Test
    fun `when open meetup page clicked emits open meetup page event`() {
        val observer = viewModel.openMeetupPage.testObserver()

        val event = defaultWtmEvent(
            id = "1",
            meetupUrl = "https://meetup.com/events/1"
        )

        mockEvent.onNext(Result(loading = false, data = event, error = null))

        viewModel.onOpenMeetupPageClicked()

        assertThat(observer.observedValues).containsExactly(
            OpenMeetupPageEvent(url = "https://meetup.com/events/1")
        )
    }
}