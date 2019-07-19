package com.wtmberlin.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.wtmberlin.data.Repository
import com.wtmberlin.data.Result
import com.wtmberlin.data.WtmEvent
import com.wtmberlin.defaultVenue
import com.wtmberlin.mock
import com.wtmberlin.util.ErrorLogger
import com.wtmberlin.util.background
import com.wtmberlin.util.io
import com.wtmberlin.util.ui
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
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

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @Mock
    private lateinit var mockRepo: Repository
    @Mock
    private lateinit var mockErrorLog: ErrorLogger
    private val viewModel by lazy { EventsViewModel(mockRepo, mockErrorLog) }

    private fun startEvents(event: WtmEvent = mock()): WtmEvent {
        Mockito.`when`(event.id).thenReturn("1")
        Mockito.`when`(event.name).thenReturn("name")
        Mockito.`when`(event.venue).thenReturn(defaultVenue(name = "name"))

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
        Dispatchers.setMain(mainThreadSurrogate)
        unconfinifyTestScope()
    }

    @Test
    fun `with upcoming Events`() {
        val upcomingEvents = startEvents()
        val dateStart = LocalDateTime.now().plusDays(1)
        Mockito.`when`(upcomingEvents.dateTimeStart).thenReturn(ZonedDateTime.of(dateStart, ZoneId.systemDefault()))

        viewModel.refreshing.observeForever { }
    }

    @Test
    fun `with no upcoming Events`() {
        val noUpcomingEvents = startEvents()
        Mockito.`when`(noUpcomingEvents.dateTimeStart).thenReturn(ZonedDateTime.now())

        viewModel.refreshing.observeForever { }
    }

    @Test
    fun `on events clicked`() {
        val clickedEvent = mock<EventItem>()
        val id = "1"
        Mockito.`when`(clickedEvent.id).thenReturn(id)

        startEventsError()

        viewModel.onEventItemClicked(clickedEvent)
    }

    @Test
    fun `on events refreshed`() {
        val eventsMocked = startEvents()
        Mockito.`when`(eventsMocked.dateTimeStart).thenReturn(ZonedDateTime.now())

        viewModel.refreshEvents()
    }

    @Test
    fun `error on data loaded`() {
        startEventsError()

        viewModel.refreshing.observeForever { }

        verify(mockErrorLog, times(1)).getException(any())
    }


    @ExperimentalCoroutinesApi
    private fun unconfinifyTestScope() {
        ui = Dispatchers.Unconfined
        io = Dispatchers.Unconfined
        background = Dispatchers.Unconfined
    }
}
