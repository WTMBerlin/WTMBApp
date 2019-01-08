package com.wtmberlin.data

import androidx.room.Room
import com.squareup.moshi.Moshi
import com.wtmberlin.CollaborationsViewModel
import com.wtmberlin.EventDetailsViewModel
import com.wtmberlin.EventsViewModel
import com.wtmberlin.meetup.DurationAdapter
import com.wtmberlin.meetup.LocalDateAdapter
import com.wtmberlin.meetup.LocalTimeAdapter
import com.wtmberlin.meetup.MeetupService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

val remoteModule = module {
    viewModel { EventsViewModel(get()) }
    viewModel { (eventId: String) -> EventDetailsViewModel(eventId, get()) }
    viewModel { CollaborationsViewModel(get()) }

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