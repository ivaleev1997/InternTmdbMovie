package com.education.core_impl.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response

object LanguageInterceptor : Interceptor {
    private const val LANGUAGE_PARAM = "language"
    private const val LANGUAGE = "ru-RUS"

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val originalHttpUrl = original.url()
        val url = originalHttpUrl
            .newBuilder()
            .addQueryParameter(
                LANGUAGE_PARAM,
                LANGUAGE
            )
            .build()

        val requestBuilder = original.newBuilder().url(url)
        val request = requestBuilder.build()

        return chain.proceed(request)
    }
}
