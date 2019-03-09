package com.wtmberlin.data

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module

val repositoryModule = module {
    single {
        ApiService(get())
    }

    single {
        Room.databaseBuilder(androidContext(), Database::class.java, "wtm-events")
            .fallbackToDestructiveMigration()
            .build()
    }

    single { createWtmEventDao(get()) }

    single {
        Repository(get(), get())
    }
}

private fun createWtmEventDao(database: Database) = database.wtmEventDAO()