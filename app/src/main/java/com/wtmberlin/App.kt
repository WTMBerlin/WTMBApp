package com.wtmberlin

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import com.wtmberlin.data.repositoryModule
import com.wtmberlin.meetup.meetupModule
import com.wtmberlin.ui.uiModule
import org.koin.android.ext.android.startKoin
import timber.log.Timber

@Suppress("unused") // Used in AndroidManifest
class App : Application() {
    override fun onCreate() {
        super.onCreate()

        AndroidThreeTen.init(this)

        startKoin(this, listOf(meetupModule, repositoryModule, uiModule))

        Timber.plant(Timber.DebugTree())
    }
}