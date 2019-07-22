package com.wtmberlin.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.wtmberlin.SetMainDispatcherRule
import com.wtmberlin.UnconfinedTestScopeRule
import com.wtmberlin.data.Repository
import com.wtmberlin.data.Result
import com.wtmberlin.data.WtmEvent
import com.wtmberlin.defaultVenue
import com.wtmberlin.mock
import com.wtmberlin.util.LogException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime

@ExperimentalCoroutinesApi
class EventsViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    var setMainDispatcherRule = SetMainDispatcherRule()
    @get:Rule
    var unconfinedTestScopeRule = UnconfinedTestScopeRule()

    @Mock
    private lateinit var mockRepo: Repository
    @Mock
    private lateinit var mockErrorLog: LogException
    private val viewModel by lazy(LazyThreadSafetyMode.NONE) { EventsViewModel(mockRepo, mockErrorLog) }

    private fun startEvents(event: WtmEvent = mock()): WtmEvent {
        given(event.id).willReturn("1")
        given(event.name).willReturn("name")
        given(event.venue).willReturn(defaultVenue(name = "name"))

        mock<Repository> {
            onBlocking {
                mockRepo.events()
            } doReturn Result(false, listOf(event), null)
        }
        return event
    }

    private fun startEventsError(exception: Exception = Exception()): Exception {
        mock<Repository> {
            onBlocking {
                mockRepo.events()
            } doReturn Result(false, null, exception)
        }
        return exception
    }

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `with upcoming Events`() {
        val upcomingEvents = startEvents()
        val dateStart = LocalDateTime.now().plusDays(1)
        given(upcomingEvents.dateTimeStart).willReturn(ZonedDateTime.of(dateStart, ZoneId.systemDefault()))

        viewModel.refreshing.observeForever { }
    }

    @Test
    fun `with no upcoming Events`() {
        val noUpcomingEvents = startEvents()
        given(noUpcomingEvents.dateTimeStart).willReturn(ZonedDateTime.now())

        viewModel.refreshing.observeForever { }
    }

    @Test
    fun `on events clicked`() {
        val clickedEvent = mock<EventItem>()
        given(clickedEvent.id).willReturn("1")

        startEventsError()

        viewModel.onEventItemClicked(clickedEvent)
    }

    @Test
    fun `on events refreshed`() {
        val eventsMocked = startEvents()
        given(eventsMocked.dateTimeStart).willReturn(ZonedDateTime.now())

        viewModel.refreshEvents()
    }

    @Test
    fun `error on data loaded`() {
        startEventsError()

        viewModel.refreshing.observeForever { }

        verify(mockErrorLog, times(1)).getException(any())
    }

}
