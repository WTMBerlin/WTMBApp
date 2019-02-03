package com.wtmberlin.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.wtmberlin.TestSchedulerProvider
import com.wtmberlin.data.*
import com.wtmberlin.mock
import com.wtmberlin.testObserver
import com.wtmberlin.util.AdapterItem
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

        val events = listOf<WtmEvent>(wtmEvent("2", "Android Co-Learning Testing part 2"),
                                      wtmEvent("3", "Android Co-Learning Testing part 3"),
                                      wtmEvent("4", "Android Co-Learning Testing part 4"))

        val expected = listOf<AdapterItem>(noUpcomingEventsHeaderItem,
                                   pastHeaderItem,
                                   eventItem("2", "Android Co-Learning Testing part 2"),
                                   eventItem("3", "Android Co-Learning Testing part 3"),
                                   eventItem("4", "Android Co-Learning Testing part 4"))

        mockEvents.onNext(Result(loading = false, data = events, error = null))

        val dataObserver = viewModel.adapterItems.testObserver()

        assertThat(dataObserver.observedValues[0])
            .isEqualTo(expected)
    }

    private fun wtmEvent(id: String, eventName: String): WtmEvent {
        return WtmEvent(id,
                        eventName,
                        ZonedDateTime.of(LocalDateTime.of(2018, 12, 18, 18, 30),
                        ZoneId.of("Europe/Paris")),
                        Duration.of(120, ChronoUnit.MINUTES),
               "amazing event", "fake url",
                        Venue("Example Company", "Example Address", Coordinates(22.0, 33.2)))
    }

    private fun eventItem(id: String, eventName: String): EventItem {
        return EventItem(id,
                         eventName,
                         LocalDateTime.of(2018, 12, 18, 18, 30),
               "Example Company")
    }
}
