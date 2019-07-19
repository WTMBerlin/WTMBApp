package com.wtmberlin.data

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {
    single {
        ApiService(get(), get())
    }

    single {
        Room.databaseBuilder(androidContext(), Database::class.java, "wtm_large-events")
            .fallbackToDestructiveMigration()
            .build()
    }

    single { createWtmEventDao(get()) }

    single {
        Repository(get(), get(), get())
    }
}

private fun createWtmEventDao(database: Database) = database.wtmEventDAO()
