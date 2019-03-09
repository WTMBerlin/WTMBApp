package com.wtmberlin.work

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.wtmberlin.data.ApiService
import com.wtmberlin.data.WtmEvent
import com.wtmberlin.data.WtmEventDao
import com.wtmberlin.notifications.Notifications
import org.threeten.bp.ZonedDateTime

class RefreshEventsWorker(
    context: Context,
    params: WorkerParameters,
    private val apiService: ApiService,
    private val eventDao: WtmEventDao,
    private val notifications: Notifications
): Worker(context, params) {
    override fun doWork(): Result {
        val oldEvents = eventDao.getAll().blockingFirst()

        if (oldEvents.isEmpty()) {
            // The database hasn't been initialized yet, so nothing to refresh. Just try again the next time.
            return Result.success()
        }

        val currentEvents = apiService.events().blockingGet()

        val now = ZonedDateTime.now()

        val oldUpcomingEvents = oldEvents.filter { it.dateTimeStart.isAfter(now) }
        val currentUpcomingEvents = currentEvents.filter { it.dateTimeStart.isAfter(now) }

        val newUpcomingEvents = mutableListOf<WtmEvent>()

        for (currentEvent in currentUpcomingEvents) {
            if (oldUpcomingEvents.find { it.id == currentEvent.id } == null) {
                newUpcomingEvents += currentEvent
            }
        }

        for (event in newUpcomingEvents) {
            notifications.showNewUpcomingEventNotification(event)
        }

        eventDao.replaceAll(currentEvents)

        return Result.success()
    }
}