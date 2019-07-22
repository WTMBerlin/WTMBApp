package com.wtmberlin.works

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.work.WorkerParameters
import com.nhaarman.mockitokotlin2.*
import com.wtmberlin.SetMainDispatcherRule
import com.wtmberlin.data.ApiService
import com.wtmberlin.data.WtmEvent
import com.wtmberlin.data.WtmEventDao
import com.wtmberlin.defaultWtmEvent
import com.wtmberlin.mock
import com.wtmberlin.notifications.Notifications
import com.wtmberlin.work.RefreshEventsWorker
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.threeten.bp.ZonedDateTime

@ExperimentalCoroutinesApi
class RefreshEventsJobTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    var setMainDispatcherRule = SetMainDispatcherRule()

    @Mock
    private lateinit var ctx: Context
    @Mock
    private lateinit var params: WorkerParameters
    @Mock
    private lateinit var apiService: ApiService
    @Mock
    private lateinit var eventDao: WtmEventDao
    @Mock
    private lateinit var notifications: Notifications

    private val work by lazy(LazyThreadSafetyMode.NONE) {
        RefreshEventsWorker(
            ctx,
            params,
            apiService,
            eventDao,
            notifications
        )
    }

    private fun startEventDb(event: WtmEvent = mock()): WtmEvent {
        given(event.id).willReturn("1")
        given(event.dateTimeStart).willReturn(ZonedDateTime.now())

        mock<WtmEventDao> {
            onBlocking {
                eventDao.getAll()
            } doReturn listOf(event)
        }
        return event
    }

    private fun startEventApi(events: List<WtmEvent> = mock()): List<WtmEvent> {
        val firstEvent = defaultWtmEvent(id = "2", dateTimeStart = ZonedDateTime.now().plusDays(1))
        val secondEvent = defaultWtmEvent(id = "3", dateTimeStart = ZonedDateTime.now().plusDays(2))
        val eventListApi = listOf(firstEvent, secondEvent)

        mock<ApiService> {
            onBlocking {
                apiService.events()
            } doReturn eventListApi
        }
        return eventListApi
    }

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        given(params.taskExecutor).willReturn(mock())
        given(params.taskExecutor.backgroundExecutor).willReturn(mock())
    }

    @Test
    fun `replaces old events in database with new events from api`() {
        startEventDb()
        val eventApi = startEventApi()

        runBlocking { work.doWork() }

        verify(eventDao, times(1)).replaceAll(eventApi)
    }

    @Test
    fun `notifies about single new upcoming event`() = runBlocking {
        val upcomingEvent = startEventApi()
        startEventDb()

        runBlocking { work.doWork() }

        verify(notifications).showNewUpcomingEventNotification(upcomingEvent[0])
    }

    @Test
    fun `notifies about multiple new upcoming events`() {
        val upcomingEvent = startEventApi()
        startEventDb()

        runBlocking { work.doWork() }

        verify(notifications).showNewUpcomingEventNotification(upcomingEvent[0])
        verify(notifications).showNewUpcomingEventNotification(upcomingEvent[1])
    }

}
