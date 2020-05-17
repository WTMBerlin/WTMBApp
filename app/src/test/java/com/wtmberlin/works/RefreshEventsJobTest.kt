package com.wtmberlin.works

import android.content.Context
import androidx.work.WorkerParameters
import com.wtmberlin.data.ApiService
import com.wtmberlin.data.WtmEventDao
import com.wtmberlin.defaultWtmEvent
import com.wtmberlin.mock
import com.wtmberlin.notifications.Notifications
import com.wtmberlin.work.RefreshEventsWorker
import io.reactivex.Flowable
import io.reactivex.Single
import org.junit.Ignore
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.threeten.bp.ZonedDateTime

class RefreshEventsJobTest {
    val context = mock<Context>()
    val params = mock<WorkerParameters>()
    val apiService = mock<ApiService>()
    val eventDao = mock<WtmEventDao>()
    val notifications = mock<Notifications>()

    val worker = RefreshEventsWorker(context, params, apiService, eventDao, notifications)

    @Test
    fun `replaces old events in database with new events from api`() {
        val eventInDatabase = defaultWtmEvent(
            id = "1",
            name = "Fun with Java"
        )

        val eventFromNetwork = defaultWtmEvent(
            id = "2",
            name = "Fun with Kotlin"
        )

        `when`(eventDao.getAll()).thenReturn(Flowable.just(listOf(eventInDatabase)))
        `when`(apiService.events()).thenReturn(Single.just(listOf(eventFromNetwork)))

        worker.doWork()

        verify(eventDao).replaceAll(listOf(eventFromNetwork))
    }

    @Test
    @Ignore("will fix after CI setup") // TODO
    fun `notifies about single new upcoming event`() {
        val newEventFromNetwork = defaultWtmEvent(
            id = "1",
            name = "Fun with Kotlin",
            dateTimeStart = tomorrow()
        )

        `when`(eventDao.getAll()).thenReturn(Flowable.just(listOf()))
        `when`(apiService.events()).thenReturn(Single.just(listOf(newEventFromNetwork)))

        worker.doWork()

        verify(notifications).showNewUpcomingEventNotification(newEventFromNetwork)
    }

    @Test
    @Ignore("will fix after CI setup") // TODO
    fun `notifies about multiple new upcoming events`() {
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

        `when`(eventDao.getAll()).thenReturn(Flowable.just(listOf()))
        `when`(apiService.events()).thenReturn(Single.just(newEventsFromNetwork))

        worker.doWork()

        verify(notifications).showNewUpcomingEventNotification(newEventsFromNetwork[0])
        verify(notifications).showNewUpcomingEventNotification(newEventsFromNetwork[1])
        verify(notifications).showNewUpcomingEventNotification(newEventsFromNetwork[2])
    }

    @Test
    @Ignore("will fix after CI setup") // TODO
    fun `notifies only about upcoming events that were not in database already`() {
        val eventInDatabase = defaultWtmEvent(
            id = "1",
            name = "Fun with Kotlin Part 1",
            dateTimeStart = tomorrow()
        )

        val newEventFromNetwork = defaultWtmEvent(
            id = "2",
            name = "Fun with Kotlin Part 2",
            dateTimeStart = tomorrow()
        )

        `when`(eventDao.getAll()).thenReturn(Flowable.just(listOf(eventInDatabase)))
        `when`(apiService.events()).thenReturn(Single.just(listOf(eventInDatabase, newEventFromNetwork)))

        worker.doWork()

        verify(notifications).showNewUpcomingEventNotification(newEventFromNetwork)
    }

    fun tomorrow() : ZonedDateTime = ZonedDateTime.now().plusDays(1)
}
