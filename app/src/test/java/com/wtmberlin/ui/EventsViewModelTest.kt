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
import org.threeten.bp.*

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
    fun `when events are not refreshed emits refresh false`() {
        val refreshObserver = viewModel.refreshing.testObserver()

        mockEvents.onNext(Result(loading = false, data = null, error = null))

        assertThat(refreshObserver.observedValues)
            .containsExactly(false)
    }

    @Test
    fun `when no events emit NoUpcomingEvents`() {
        val observer = viewModel.adapterItems.testObserver()

        mockEvents.onNext(Result(loading = false, data = emptyList(), error = null))

        assertThat(observer.observedValues)
            .containsExactly(listOf(NoUpcomingEventsItem))
    }

    @Test
    fun `when one past event emit past header item and event`() {
        val input = listOf(wtmEvent("2", "DevOps", yesterday(), "CompanyA"))
        val expected = listOf(
            NoUpcomingEventsItem,
            PastHeaderItem,
            EventItem("2", "DevOps", yesterday(), "CompanyA")
        )

        val observer = viewModel.adapterItems.testObserver()

        mockEvents.onNext(Result(loading = false, data = input, error = null))

        assertThat(observer.observedValues).containsExactly(expected)
    }

    @Test
    fun `when one upcoming event emit upcoming header item and event`() {
        val input = listOf(wtmEvent("2", "DevOps", tomorrow(), "CompanyA"))
        val expected = listOf(
            UpcomingHeaderItem,
            EventItem("2", "DevOps", tomorrow(), "CompanyA")
        )
        val observer = viewModel.adapterItems.testObserver()

        mockEvents.onNext(Result(loading = false, data = input, error = null))

        assertThat(observer.observedValues).containsExactly(expected)
    }

    @Test
    fun `when one upcoming and one past event emit upcoming header item, upcoming event, past header item, past event`() {
        val input = listOf(
            wtmEvent("2", "DevOps", tomorrow(), "CompanyA"),
            wtmEvent("1", "Android Study Jam", yesterday(), "CompanyB")
        )
        val expected = listOf(
            UpcomingHeaderItem,
            EventItem("2", "DevOps", tomorrow(), "CompanyA"),
            PastHeaderItem,
            EventItem("1", "Android Study Jam", yesterday(), "CompanyB")
        )

        val observer = viewModel.adapterItems.testObserver()

        mockEvents.onNext(Result(loading = false, data = input, error = null))

        assertThat(observer.observedValues).containsExactly(expected)
    }


    fun wtmEvent(id: String, name: String, dateTime: LocalDateTime, venueName: String): WtmEvent {
        return WtmEvent(
            id,
            name,
            ZonedDateTime.of(dateTime, ZoneId.systemDefault()),
            Duration.ofMinutes(120),
            "amazing event",
            "https://www.meetup.com/de-DE/Women-Techmakers-Berlin/events/258092838/",
            "https://www.meetup.com/event/123456",
            Venue(venueName, "Example Address", Coordinates(22.0, 33.2))
        )
    }

    fun yesterday() = LocalDateTime.of(LocalDate.now(), LocalTime.NOON).minusDays(1)

    fun tomorrow() = LocalDateTime.of(LocalDate.now(), LocalTime.NOON).plusDays(1)
}
