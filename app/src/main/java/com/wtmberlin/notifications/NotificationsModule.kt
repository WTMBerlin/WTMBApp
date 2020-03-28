package com.wtmberlin.notifications

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val notificationsModule = module {
    single { Notifications(androidContext()) }
}