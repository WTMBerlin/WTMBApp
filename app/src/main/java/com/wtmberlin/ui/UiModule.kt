package com.wtmberlin.ui

import com.wtmberlin.util.LogException
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val uiModule = module {

    single { LogException() }

    viewModel { EventsViewModel(get(), get()) }
    viewModel { StatsViewModel(get()) }
    viewModel { (eventId: String) -> EventDetailsViewModel(eventId, get(), get()) }

    viewModel { CollaborationsViewModel(get(), get()) }

}
