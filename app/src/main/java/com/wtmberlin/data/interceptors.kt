package com.wtmberlin.data

import okhttp3.Interceptor
import okhttp3.Response

object AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val newRequest = request.newBuilder()
            .header("Authorization", "Bearer $accessToken")
            .build()

        return chain.proceed(newRequest)
    }

}