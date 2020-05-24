@file:Suppress("DEPRECATION")

package com.wtmberlin.work

import androidx.work.Configuration
import androidx.work.WorkManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val workModule = module {
    single {
        val configuration = Configuration.Builder()
            .setWorkerFactory(WtmWorkerFactory(get(), get(), get()))
            .build()

        WorkManager.initialize(androidContext(), configuration)

        Works(WorkManager.getInstance())
    }
}
