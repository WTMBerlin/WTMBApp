package com.wtmberlin.data

import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

val remoteModule = module {
    single {
        Repository(get())
    }

    single {
        get<Retrofit>().create(MeetupService::class.java)
    }

    single {
        Retrofit.Builder()
            .baseUrl("api.meetup.com")
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(get())
            .build()
    }

    single {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply { level = BODY })
            .build()
    }

    single {
        Moshi.Builder()
            .add(LocalDateAdapter())
            .add(LocalTimeAdapter())
            .build()
    }
}