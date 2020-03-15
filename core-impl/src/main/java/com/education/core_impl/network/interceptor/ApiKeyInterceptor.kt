package com.education.core_impl.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response

object ApiKeyInterceptor : Interceptor {
    private const val TMDB_API_KEY = "aa7b2f0df06cb6ccf1cbcf705bcf9892"
    private const val API_KEY_PARAM = "api_key"

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val originalHttpUrl = original.url()
        val url = originalHttpUrl
            .newBuilder()
            .addQueryParameter(
                API_KEY_PARAM,
                TMDB_API_KEY
            )
            .build()

        val requestBuilder = original.newBuilder().url(url)
        val request = requestBuilder.build()

        return chain.proceed(request)
    }
}