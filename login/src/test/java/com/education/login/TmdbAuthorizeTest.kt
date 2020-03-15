package com.education.login

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import com.education.core_api.TMDB_BASE_URL
import com.education.core_api.dto.User
import com.education.core_api.network.TmdbAuthApi
import com.education.login.repository.LoginRepository
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowLog
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
class TmdbAuthorizeTest {

    lateinit var sharedPrefs: SharedPreferences
    lateinit var tmdbAuthApi: TmdbAuthApi
    lateinit var loginRepository: LoginRepository
    private val appContext = RuntimeEnvironment.application.applicationContext

    @Before
    fun setUp() {
        ShadowLog.setupLogging()
        val client = OkHttpClient.Builder().apply {
            addInterceptor(ApiKeyInterceptor)
            addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        }.build()

        tmdbAuthApi = Retrofit.Builder()
            .baseUrl(TMDB_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(
                GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .create()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(TmdbAuthApi::class.java)

        sharedPrefs = appContext.getSharedPreferences("APP_SHARED", Context.MODE_PRIVATE)

        loginRepository = LoginRepository(
            tmdbAuthApi,
            sharedPrefs
        )
    }

    @Test
    fun login_Test() {
        val result = loginRepository
            .login(User("nekitpip@mail.ru", "q1w2e3r4"))
            .blockingGet()

        assertTrue(result)
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