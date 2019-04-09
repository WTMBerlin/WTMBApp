package com.wtmberlin.work

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.wtmberlin.data.ApiService
import com.wtmberlin.data.WtmEventDao
import com.wtmberlin.notifications.Notifications

class WtmWorkerFactory(
    private val apiService: ApiService,
    private val eventDao: WtmEventDao,
    private val notifications: Notifications
) : WorkerFactory() {
    override fun createWorker(context: Context, className: String, params: WorkerParameters) : ListenableWorker? {
        return when (className) {
            RefreshEventsWorker::class.java.name -> RefreshEventsWorker(context, params, apiService, eventDao, notifications)
            else -> null
        }
    }
}