package com.wtmberlin.ui

import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val uiModule = module {

    viewModel { EventsViewModel(get(), get()) }
    viewModel { (eventId: String) -> EventDetailsViewModel(eventId, get(), get()) }
    viewModel { CollaborationsViewModel(get(), get()) }
}