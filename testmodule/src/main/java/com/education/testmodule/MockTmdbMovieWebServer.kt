package com.education.testmodule

import com.education.core_api.data.network.TmdbMovieApi
import com.education.core_impl.data.network.interceptor.NetworkErrorInterceptor
import com.education.core_impl.di.module.NetworkModule
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MockTmdbMovieWebServer {
    val mockWebServer = MockWebServer()
    val tmdbMovieApi: TmdbMovieApi

    init {
        val networkModule = NetworkModule()
        val gson = networkModule.provideGson()
        val client = OkHttpClient.Builder()
            .authenticator(networkModule.provideTmdbAuthenticator())
            .addInterceptor(NetworkErrorInterceptor(gson))
            .build()

        tmdbMovieApi = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .client(client)
            .addConverterFactory(
                GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(TmdbMovieApi::class.java)
    }

    fun setDispatcher(dispatcher: Dispatcher) {
        mockWebServer.dispatcher = dispatcher
    }

    fun enqueue(response: MockResponse) {
        mockWebServer.enqueue(response)
    }
}
