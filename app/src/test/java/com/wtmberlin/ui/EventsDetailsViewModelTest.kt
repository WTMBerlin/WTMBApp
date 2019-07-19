package com.wtmberlin.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.wtmberlin.data.*
import com.wtmberlin.util.ErrorLogger
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.threeten.bp.Duration
import org.threeten.bp.ZonedDateTime

class EventsDetailsViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @MockK
    internal lateinit var repository: Repository
    @MockK
    internal lateinit var errorLogger: ErrorLogger
    private val viewModel by lazy { EventDetailsViewModel("1", repository, errorLogger) }
    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun before() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `fetch event with given id`() {
        val event = startEvent()

        viewModel.event.observeForever { }

        assertEquals(event, viewModel.event.value)
    }

    @Test
    fun `fetch event with no venue`() {
        val event = mockk<WtmEvent>()
        val venue = null
        every { event.venue } returns venue
        startEvent(event)

        viewModel.onLocationClicked()
        viewModel.event.observeForever { }

        val result = viewModel.event.value
        assertEquals(venue, result?.venue)
    }

    @Test
    fun `on time clicked`() {
        val event = mockk<WtmEvent>()
        val venue = mockk<Venue>()
        val startTime = ZonedDateTime.now()
        val eventName = "eventName"
        val venueName = "venueName"
        every { event.venue } returns venue
        every { event.dateTimeStart } returns startTime
        every { event.duration } returns Duration.ofMillis(1000)
        every { event.name } returns eventName
        every { venue.name } returns venueName
        startEvent(event)

        viewModel.onDateTimeClicked()
        viewModel.addToCalendar.observeForever { }

        val result = viewModel.addToCalendar.value
        assertEquals(eventName, result?.title)
        assertEquals(venueName, result?.location)
        assertEquals(startTime.toInstant().toEpochMilli(), result?.beginTime)
        assertEquals(startTime.toInstant().toEpochMilli() + 1000, result?.endTime)
    }

    @Test
    fun `on time clicked with no event`() {
        startEvent(null)

        viewModel.onDateTimeClicked()
        viewModel.addToCalendar.observeForever { }

    }

    @Test
    fun `on location clicked`() {
        val event = mockk<WtmEvent>()
        val venue = mockk<Venue>()
        val coordLat = 0.0
        val coordLong = 1.1
        val venueName = "venueName"
        every { event.venue } returns venue
        every { venue.name } returns venueName
        every { venue.coordinates } returns Coordinates(coordLat, coordLong)
        startEvent(event)

        viewModel.onLocationClicked()
        viewModel.openMaps.observeForever { }

        val result = viewModel.openMaps.value
        assertEquals(venueName, result?.venueName)
        assertEquals(Coordinates(coordLat, coordLong), result?.coordinates)
    }

    @Test
    fun `on location clicked with no coordinates`() {
        val event = mockk<WtmEvent>()
        val venue = mockk<Venue>()
        val coordinates = null
        every { event.venue } returns venue
        every { venue.coordinates } returns coordinates
        startEvent(event)

        viewModel.onLocationClicked()
        viewModel.openMaps.observeForever { }

        val result = viewModel.openMaps.value
        assertEquals(coordinates, result?.coordinates)
    }

    @Test
    fun `on location clicked with no event`() {
        startEvent(null)

        viewModel.onLocationClicked()
        viewModel.openMaps.observeForever { }
    }

    @Test
    fun `on meetUp page clicked`() {
        val event = mockk<WtmEvent>()
        val url = "www.google.com"
        every { event.meetupUrl } returns url
        startEvent(event)

        viewModel.onOpenMeetupPageClicked()
        viewModel.openMeetupPage.observeForever { }

        val resultMeetup = viewModel.openMeetupPage.value
        assertEquals(url, resultMeetup?.url)
    }

    @Test
    fun `on meetUp page clicked with no event`() {
        startEvent(null)

        viewModel.onOpenMeetupPageClicked()
        viewModel.openMeetupPage.observeForever { }
    }

    private fun startEvent(event: WtmEvent? = mockk()): WtmEvent? {
        coEvery { repository.event("1") } returns Result(false, event, null)
        viewModel.event.observeForever {}
        return event
    }
}