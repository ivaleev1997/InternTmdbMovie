package com.education.login

import com.education.core_api.network.TmdbAuthApi
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MockTmdbAuthWebServer {
    private val mockWebServer = MockWebServer()
    val tmdbAuthApi: TmdbAuthApi

    init {
        val client = OkHttpClient.Builder().apply {
            addInterceptor(ApiKeyInterceptor)
            addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        }.build()

        tmdbAuthApi = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .client(client)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                        .create()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(TmdbAuthApi::class.java)
    }

    fun enqueue(response: MockResponse) {
        mockWebServer.enqueue(response)
    }
}

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