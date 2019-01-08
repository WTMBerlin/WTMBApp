package com.wtmberlin.data

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module

val repositoryModule = module {
    single {
        val database = Room.databaseBuilder(
            androidContext(),
            Database::class.java, "wtm-events"
        )
            .fallbackToDestructiveMigration()
            .build()

        Repository(get(), database)
    }
}