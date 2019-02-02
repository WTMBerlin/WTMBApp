package com.wtmberlin.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.wtmberlin.TestSchedulerProvider
import com.wtmberlin.data.*
import com.wtmberlin.mock
import com.wtmberlin.testObserver
import io.reactivex.processors.PublishProcessor
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.`when`
import org.threeten.bp.Duration
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.temporal.ChronoUnit

class EventsViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    val mockEvents = PublishProcessor.create<Result<List<WtmEvent>>>()
    val mockRepository = mock<Repository>().also {
        `when`(it.events()).thenReturn(mockEvents)
    }

    val viewModel = EventsViewModel(mockRepository, TestSchedulerProvider())

    @Test
    fun `when events are refreshed emits refresh true`() {
        val refreshObserver = viewModel.refreshing.testObserver()

        mockEvents.onNext(Result(loading = true, data = null, error = null))

        assertThat(refreshObserver.observedValues)
            .containsExactly(true)
    }

    @Test
    fun `when events are refreshed emits refresh false`() {
        val refreshObserver = viewModel.refreshing.testObserver()

        mockEvents.onNext(Result(loading = false, data = null, error = null))

        assertThat(refreshObserver.observedValues)
            .containsExactly(false)
    }

    @Test
    fun `on WtmEvents parsed to EventItems`() {

        //EventsFragment
        val noUpcomingEventsHeaderItem = NoUpcomingEventsItem
        val pastHeaderItem = PastHeaderItem

        val events: List<WtmEvent> = listOf<WtmEvent>(
            createWtmEvent("2", "Android Co-Learning Testing part 2"),
            createWtmEvent("3", "Android Co-Learning Testing part 3"),
            createWtmEvent("4", "Android Co-Learning Testing part 4")
        )

        val result = listOf<Any>(
            noUpcomingEventsHeaderItem,
            pastHeaderItem,
            createEventItem("2", "Android Co-Learning Testing part 2"),
            createEventItem("3", "Android Co-Learning Testing part 3"),
            createEventItem("4", "Android Co-Learning Testing part 4")
        )

        mockEvents.onNext(Result(loading = false, data = events, error = null))

        val dataObserver = viewModel.adapterItems.testObserver()

        assertThat(dataObserver.observedValues[0])
            .isEqualTo(result)
    }

    private fun convertWtmEventToEventItem(event: WtmEvent): EventItem {
        return EventItem(event.id, event.name, event.dateTimeStart.toLocalDateTime(), event.venue!!.name)
    }

    private fun createEventItem(id: String, eventName: String): EventItem {
        val wtmEvent = createWtmEvent(id, eventName)
        return convertWtmEventToEventItem(wtmEvent)
    }

    private fun createWtmEvent(id: String, eventName: String): WtmEvent {
        val exampleDate = LocalDateTime.of(2018, 12, 18, 18, 30)
        val exampleZonedDate = ZonedDateTime.of(exampleDate, ZoneId.of("Europe/Paris"))
        val exampleDuration = Duration.of(120, ChronoUnit.MINUTES)
        val description = "amazing event"
        val url = "fake url"
        val exampleVenue = Venue("Example Company", "Example Address", Coordinates(22.0, 33.2))
        return WtmEvent(id, eventName, exampleZonedDate, exampleDuration, url, description, exampleVenue)
    }
}
