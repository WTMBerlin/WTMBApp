package com.wtmberlin.works

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.work.WorkerParameters
import com.wtmberlin.data.ApiService
import com.wtmberlin.data.WtmEventDao
import com.wtmberlin.defaultWtmEvent
import com.wtmberlin.notifications.Notifications
import com.wtmberlin.work.RefreshEventsWorker
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.threeten.bp.ZonedDateTime

class RefreshEventsJobTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @MockK
    internal lateinit var ctx: Context
    @MockK
    private val params = mockk<WorkerParameters>()
    @MockK
    internal lateinit var apiService: ApiService
    @MockK
    private lateinit var eventDao: WtmEventDao
    @MockK
    private lateinit var notifications: Notifications

    private val work by lazy { RefreshEventsWorker(ctx, params, apiService, eventDao, notifications) }
    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)

        every { params.taskExecutor } returns mockk()
        every { params.taskExecutor.backgroundExecutor } returns mockk()
    }

    @Test
    fun `replaces old events in database with new events from api`() = runBlocking {
        val eventDb = defaultWtmEvent(id = "1", name = "No Idea", dateTimeStart = ZonedDateTime.now())
        val eventApi1 = eventDb.copy(id = "15")
        val eventApi2 = eventDb.copy(id = "14")
        val listApi = listOf(eventApi1, eventApi2)
        coEvery { eventDao.getAll() } returns listOf(eventDb)
        coEvery { apiService.events() } returns listApi

        work.doWork()

        verify { eventDao.replaceAll(listApi) }
    }

    @Test
    fun `notifies about single new upcoming event`() = runBlocking {
        val event = defaultWtmEvent(id = "23", name = "Kt", dateTimeStart = tomorrow())
        val oldEvent = event.copy(id = "0", dateTimeStart = ZonedDateTime.now().minusDays(1))

        coEvery { eventDao.getAll() } returns listOf(oldEvent)
        coEvery { apiService.events() } returns listOf(event)

        work.doWork()

        verify { notifications.showNewUpcomingEventNotification(event) }
    }

    @Test
    fun `notifies about multiple new upcoming events`() = runBlocking {
        val newEventsFromNetwork = listOf(
            defaultWtmEvent(
                id = "2",
                name = "Fun with Kotlin Part 1",
                dateTimeStart = tomorrow()
            ),
            defaultWtmEvent(
                id = "3",
                name = "Fun with Kotlin Part 2",
                dateTimeStart = tomorrow()
            ),
            defaultWtmEvent(
                id = "4",
                name = "Fun with Kotlin Part 3",
                dateTimeStart = tomorrow()
            )
        )
        coEvery { eventDao.getAll() } returns listOf(defaultWtmEvent(id = "15"))
        coEvery { apiService.events() } returns newEventsFromNetwork

        work.doWork()

        verify { notifications.showNewUpcomingEventNotification(newEventsFromNetwork[0]) }
        verify { notifications.showNewUpcomingEventNotification(newEventsFromNetwork[1]) }
        verify { notifications.showNewUpcomingEventNotification(newEventsFromNetwork[2]) }
    }

    private fun tomorrow(): ZonedDateTime = ZonedDateTime.now().plusDays(1)

}