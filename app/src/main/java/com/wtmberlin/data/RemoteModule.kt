package com.wtmberlin.data

import androidx.room.Room
import com.squareup.moshi.Moshi
import com.wtmberlin.CollaborationsViewModel
import com.wtmberlin.EventDetailsViewModel
import com.wtmberlin.EventsViewModel
import com.wtmberlin.MeetupAuthViewModel
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
    viewModel { MeetupAuthViewModel(get()) }
    viewModel { CollaborationsViewModel(get()) }

    single {
        Repository(get(), get())
    }

    single {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply { level = BODY })
            //.addInterceptor(AuthInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.meetup.com")
            .addConverterFactory(MoshiConverterFactory.create(get()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()

        retrofit.create(MeetupService::class.java)
    }

    single {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply { level = BODY })
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://secure.meetup.com")
            .addConverterFactory(MoshiConverterFactory.create(get()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()

        retrofit.create(SecureMeetupService::class.java)
    }

    single {
        Moshi.Builder()
            .add(LocalDateAdapter())
            .add(LocalTimeAdapter())
            .add(DurationAdapter())
            .build()
    }

    single {
        Room.databaseBuilder(
            androidContext(),
            EventDatabase::class.java, "wtm-events"
        ).build()
    }
}