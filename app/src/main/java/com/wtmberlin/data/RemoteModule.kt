package com.wtmberlin.data

import androidx.room.Room
import com.wtmberlin.ui.CollaborationsViewModel
import com.wtmberlin.ui.EventDetailsViewModel
import com.wtmberlin.ui.EventsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val remoteModule = module {
    single {
        Repository(get(), get())
    }

    single {
        Room.databaseBuilder(
            androidContext(),
            EventDatabase::class.java, "wtm-events"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}