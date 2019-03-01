package com.wtmberlin.ui

import com.wtmberlin.SchedulerProvider
import com.wtmberlin.StandardSchedulerProvider
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val uiModule = module {
    single<SchedulerProvider> { StandardSchedulerProvider() }

    viewModel { EventsViewModel(get(), get()) }
    viewModel { (eventId: String) -> EventDetailsViewModel(eventId, get(), get()) }
    viewModel { CollaborationsViewModel(get()) }
}