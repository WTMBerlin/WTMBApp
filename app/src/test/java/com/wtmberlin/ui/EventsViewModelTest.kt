package com.wtmberlin.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.wtmberlin.data.Repository
import com.wtmberlin.data.Result
import com.wtmberlin.data.WtmEvent
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
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime

class EventsViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @MockK
    internal lateinit var repository: Repository
    private val viewModel by lazy { EventsViewModel(repository) }
    private val testDispatcher = TestCoroutineDispatcher()

    private val simpleEvent = mockk<WtmEvent>().also {
        every { it.id } returns "1"
        every { it.dateTimeStart } returns ZonedDateTime.now()
        every { it.name } returns "nameEvent"
        every { it.venue } returns null
        every { it.meetupUrl } returns "www.google.com"
    }

    private fun startEvents(event: WtmEvent = simpleEvent): WtmEvent {
        coEvery { repository.events() } returns Result(false, listOf(event), null)
        return event
    }

    private val simpleUpcomingEvent = mockk<WtmEvent>().also {
        val dateStart = LocalDateTime.now().plusDays(1)
        every { it.id } returns "1"
        every { it.dateTimeStart } returns ZonedDateTime.of(dateStart, ZoneId.systemDefault())
        every { it.name } returns "nameEvent"
        every { it.venue } returns null
        every { it.meetupUrl } returns "www.google.com"
    }

    private fun startUpcomingEvents(event: WtmEvent = simpleUpcomingEvent): WtmEvent {
        coEvery { repository.events() } returns Result(false, listOf(event), null)
        return event
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `with upcoming Events`() {
        startUpcomingEvents()
        viewModel.refreshEvents()
    }

    @Test
    fun `on events clicked`() {
        val eventItem = mockk<EventItem>()
        val id = "1"
        every { eventItem.id } returns id
        startEvents()

        viewModel.onEventItemClicked(eventItem)
        viewModel.displayEventDetails.observeForever { }

        val result = viewModel.displayEventDetails.value
        assertEquals(id, result?.eventId)
    }

    @Test
    fun `on events refreshed`() {
        startEvents()

        viewModel.refreshEvents()
    }

}
