package com.wtmberlin

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import com.wtmberlin.data.repositoryModule
import com.wtmberlin.meetup.meetupModule
import com.wtmberlin.notifications.Notifications
import com.wtmberlin.notifications.notificationsModule
import com.wtmberlin.ui.AppPreferences
import com.wtmberlin.ui.uiModule
import com.wtmberlin.work.Works
import com.wtmberlin.work.workModule
import org.koin.android.ext.android.inject
import org.koin.android.ext.android.startKoin
import timber.log.Timber

@Suppress("unused") // Used in AndroidManifest
class App : Application() {
    private val notifications : Notifications by inject()
    private val works : Works by inject()

    override fun onCreate() {
        super.onCreate()

        AndroidThreeTen.init(this)

        startKoin(this, listOf(meetupModule, notificationsModule, repositoryModule, uiModule, workModule))

        Timber.plant(Timber.DebugTree())

        notifications.init()

        works.schedulePeriodicEventRefresh()
        AppPreferences.init(this)
    }
}