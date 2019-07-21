package com.wtmberlin.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.*
import com.wtmberlin.SetMainDispatcherRule
import com.wtmberlin.data.Coordinates
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
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.threeten.bp.Duration
import org.threeten.bp.ZonedDateTime
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class EventsDetailsViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    var setMainDispatcherRule = SetMainDispatcherRule()

    @Mock
    private lateinit var mockRepository: Repository
    @Mock
    private lateinit var mockErrorLogger: ErrorLogger
    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        EventDetailsViewModel(
            "1",
            mockRepository,
            mockErrorLogger
        )
    }

    private fun startEvent(event: WtmEvent? = mock(), eventId: String? = null): WtmEvent? {
        mock<Repository> {
            onBlocking {
                mockRepository.event(eventId ?: any())
            } doReturn Result(false, event, null)
        }
        viewModel.event.observeForever { }
        return event
    }

    private fun startEventError(
        exception: java.lang.Exception = java.lang.Exception(),
        eventId: String? = null
    ): java.lang.Exception {
        mock<Repository> {
            onBlocking {
                mockRepository.event(eventId ?: any())
            } doReturn Result(false, null, exception)
        }
        viewModel.event.observeForever { }
        return exception
    }

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        unconfinifyTestScope()
        doNothing().`when`(mockErrorLogger).getException(any())
    }

    @Test
    fun `emits event with given id`() = runBlocking {
        val event = startEvent()

        val result = viewModel.event.value
        assertEquals(event, result)
    }

    @Test
    fun `when datetime clicked with no event`() = runBlocking {
        val event = startEvent(null)

        viewModel.onDateTimeClicked()

        val result = viewModel.event.value
        assertEquals(event, result)
    }

    @Test
    fun `when datetime clicked emits add to calendar event`() = runBlocking {
        val eventMocked = startEvent()
        val dateTimeStart = ZonedDateTime.now()
        val duration = Duration.ofHours(3)
        given(eventMocked?.id).willReturn("1")
        given(eventMocked?.name).willReturn("Fun with Kotlin")
        given(eventMocked?.dateTimeStart).willReturn(dateTimeStart)
        given(eventMocked?.duration).willReturn(duration)
        given(eventMocked?.venue).willReturn(defaultVenue(name = "Google Berlin"))

        viewModel.onDateTimeClicked()

        val result = viewModel.event.value
        assertEquals(eventMocked, result)
    }

    @Test
    fun `when location clicked map event with no coordinates`() = runBlocking {
        val eventMocked = startEvent()
        given(eventMocked?.id).willReturn("1")
        given(eventMocked?.venue).willReturn(defaultVenue(name = "Google Berlin", coordinates = null))

        viewModel.onLocationClicked()

        val result = viewModel.event.value
        assertEquals(eventMocked?.venue?.coordinates, result?.venue?.coordinates)
    }

    @Test
    fun `when location clicked emits open map event`() = runBlocking {
        val eventMocked = startEvent()
        given(eventMocked?.id).willReturn("1")
        given(eventMocked?.venue).willReturn(
            defaultVenue(
                name = "Google Berlin",
                coordinates = Coordinates(12.34, 56.78)
            )
        )

        viewModel.onLocationClicked()

        val result = viewModel.event.value
        assertEquals(eventMocked?.venue?.coordinates, result?.venue?.coordinates)
    }

    @Test
    fun `when open meetup page clicked emits open meetup page event`() = runBlocking {
        val eventMocked = startEvent()
        given(eventMocked?.id).willReturn("1")
        given(eventMocked?.meetupUrl).willReturn("https://meetup.com/events/1")

        viewModel.onOpenMeetupPageClicked()

        val result = viewModel.event.value
        assertEquals(eventMocked, result)
    }

    @Test
    fun `emits event with given id with error`() = runBlocking {
        startEventError(eventId = "1")

        verify(mockErrorLogger, times(1)).getException(any())
    }

    @ExperimentalCoroutinesApi
    private fun unconfinifyTestScope() {
        ui = Dispatchers.Unconfined
        io = Dispatchers.Unconfined
        background = Dispatchers.Unconfined
    }
}