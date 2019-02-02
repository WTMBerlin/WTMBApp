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
        val exampleDate = LocalDateTime.of(2018, 12, 18, 18, 30)
        val exampleZonedDate = ZonedDateTime.of(exampleDate, ZoneId.of("Europe/Paris"))
        val exampleDuration = Duration.of(120, ChronoUnit.MINUTES)
        val exampleVenue = Venue("Example Company", "Example Address", Coordinates(22.0,33.2))

        //EventsFragment
        val noUpcomingEventsHeaderItem = NoUpcomingEventsItem
        val pastHeaderItem = PastHeaderItem
        val result = mutableListOf<Any>()

        val events: MutableList<WtmEvent> = mutableListOf<WtmEvent>()

        events.add(0,element = WtmEvent("2","Android Co-Learning Testing part 2", exampleZonedDate,exampleDuration,"amazing event","fake url",exampleVenue))
        events.add(1,element = WtmEvent("3","Android Co-Learning Testing part 3", exampleZonedDate,exampleDuration,"amazing event","fake url",exampleVenue))
        events.add(2,element = WtmEvent("4","Android Co-Learning Testing part 4", exampleZonedDate,exampleDuration,"amazing event","fake url",exampleVenue))

        result.add(0,noUpcomingEventsHeaderItem)
        result.add(1,pastHeaderItem)
        result.add(2,EventItem("2","Android Co-Learning Testing part 2", exampleZonedDate.toLocalDateTime(),exampleVenue.name))
        result.add(3,EventItem("3","Android Co-Learning Testing part 3", exampleZonedDate.toLocalDateTime(),exampleVenue.name))
        result.add(4,EventItem("4","Android Co-Learning Testing part 4", exampleZonedDate.toLocalDateTime(),exampleVenue.name))

        mockEvents.onNext(Result(loading = false, data = events, error = null))

        val dataObserver = viewModel.adapterItems.testObserver()

        assertThat(dataObserver.observedValues[0])
            .isEqualTo(result)
    }
}
