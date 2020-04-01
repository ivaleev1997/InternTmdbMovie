package com.education.testmodule

import com.education.core_api.data.network.TmdbAuthApi
import com.education.core_impl.data.network.interceptor.NetworkErrorInterceptor
import com.education.core_impl.di.module.NetworkModule
import java.net.HttpURLConnection
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MockTmdbAuthWebServer {
    val mockWebServer = MockWebServer()
    val tmdbAuthApi: TmdbAuthApi

    init {
        val networkModule = NetworkModule()
        val gson = networkModule.provideGson()
        val client = OkHttpClient.Builder()
            .authenticator(networkModule.provideTmdbAuthenticator())
            .addInterceptor(NetworkErrorInterceptor(gson))
            .build()

        tmdbAuthApi = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .client(client)
            .addConverterFactory(
                GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(TmdbAuthApi::class.java)
    }

    fun setDispatcher(dispatcher: Dispatcher) {
        mockWebServer.dispatcher = dispatcher
    }

    fun enqueue(response: MockResponse) {
        mockWebServer.enqueue(response)
    }
}

object SuccessDispatcher : Dispatcher() {
    override fun dispatch(request: RecordedRequest): MockResponse {
        return when (request.path) {
            "/authentication/token/new" -> MockResponse().setResponseCode(HttpURLConnection.HTTP_OK).setBody(
                requestTokenResponseBody
            )
            "/authentication/session/new" -> MockResponse().setResponseCode(HttpURLConnection.HTTP_OK).setBody(
                createSessionIdResponseBody
            )
            "/authentication/token/validate_with_login" -> MockResponse().setResponseCode(
                HttpURLConnection.HTTP_OK
            ).setBody(createSessionWithLoginResponseBody)

            else -> MockResponse().setResponseCode(HttpURLConnection.HTTP_NOT_FOUND)
        }
    }
}

object ErrorUnAuthorizeDispatcher : Dispatcher() {
    override fun dispatch(request: RecordedRequest): MockResponse {
        return when (request.path) {
            "/authentication/token/new" -> MockResponse().setResponseCode(HttpURLConnection.HTTP_UNAUTHORIZED).setBody(
                errorResponseBody
            )
            else -> MockResponse().setResponseCode(HttpURLConnection.HTTP_NOT_FOUND)
        }
    }
}
