package com.wtmberlin.work

import androidx.work.*
import org.threeten.bp.Duration
import java.util.concurrent.TimeUnit

class Works(private val workManager: WorkManager) {
    companion object {
        private const val EVENT_REFRESH_NAME = "EVENT_REFRESH"

        private val EVENT_REFRESH_INTERVAL = Duration.ofHours(24)
    }

    fun schedulePeriodicEventRefresh() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val work = PeriodicWorkRequestBuilder<RefreshEventsWorker>(EVENT_REFRESH_INTERVAL.seconds, TimeUnit.SECONDS)
            .setConstraints(constraints)
            .build()

        workManager.enqueueUniquePeriodicWork(
            EVENT_REFRESH_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            work
        )
    }
}