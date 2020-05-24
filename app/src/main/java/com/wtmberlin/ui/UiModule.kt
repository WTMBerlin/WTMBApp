package com.wtmberlin.ui

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import com.wtmberlin.util.LogException

val uiModule = module {

    single { LogException() }

    viewModel { EventsViewModel(get(), get()) }
    viewModel { StatsViewModel(get()) }
    viewModel { (eventId: String) -> EventDetailsViewModel(eventId, get(), get()) }

    viewModel { CollaborationsViewModel(get(), get()) }

}
