package com.education.login

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import com.education.core_api.REQUEST_LIFE
import com.education.core_api.REQUEST_TOKEN
import com.education.core_api.SESSION
import com.education.core_api.dto.User
import com.education.login.repository.LoginRepository
import io.reactivex.schedulers.TestScheduler
import java.net.HttpURLConnection.HTTP_OK
import java.util.concurrent.TimeUnit
import okhttp3.mockwebserver.MockResponse
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowLog

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
class TmdbAuthorizeTest {

    lateinit var sharedPrefs: SharedPreferences
    lateinit var loginRepository: LoginRepository
    lateinit var mockTmdbAuthWebServer: MockTmdbAuthWebServer
    private val appContext = RuntimeEnvironment.application.applicationContext

    @Before
    fun setUp() {
        ShadowLog.setupLogging()
        mockTmdbAuthWebServer = MockTmdbAuthWebServer()
        sharedPrefs = appContext.getSharedPreferences("APP_SHARED", Context.MODE_PRIVATE)

        loginRepository = LoginRepository(
            mockTmdbAuthWebServer.tmdbAuthApi,
            sharedPrefs
        )
    }

    @Test
    fun loginSuccess_Test() {
        val expectedRequestToken = "f6dfeab156c7317faa24aee64360f9194b34018d"
        val expectedSessionId = "f7a0c6e5f837f5c805581b4397c02b4f0b5f89cd"
        val expectedRequestLifeTime = 1584471533000

        mockTmdbAuthWebServer.enqueue(
            MockResponse()
                .setResponseCode(HTTP_OK)
                .setBody(requestTokenResponseBody)
        )
        mockTmdbAuthWebServer.enqueue(
            MockResponse()
                .setResponseCode(HTTP_OK)
                .setBody(createSessionWithLoginResponseBody)
        )
        mockTmdbAuthWebServer.enqueue(
            MockResponse()
                .setResponseCode(HTTP_OK)
                .setBody(createSessionIdResponseBody)
        )
        val testScheduler = TestScheduler()
        val testObserver = loginRepository
            .login(User("nekitpip@mail.ru", "q1w2e3r4"))
            .subscribeOn(testScheduler)
            .observeOn(testScheduler)
            .test()

        testScheduler.advanceTimeBy(1L, TimeUnit.SECONDS)
        testObserver.assertValue(true)
        testObserver.dispose()

        val actualRequestToken = sharedPrefs.getString(REQUEST_TOKEN, null)
        val actualSessionId = sharedPrefs.getString(SESSION, null)
        val actualRequestLifeTime = sharedPrefs.getLong(REQUEST_LIFE, 0L)

        assertEquals(actualRequestToken, expectedRequestToken)
        assertEquals(actualSessionId, expectedSessionId)
        assertEquals(actualRequestLifeTime, expectedRequestLifeTime)
    }

    @Test
    fun loginFalse_Test() {
        mockTmdbAuthWebServer.enqueue(
            MockResponse()
                .setResponseCode(HTTP_OK)
                .setBody(requestTokenResponseBody)
        )
        mockTmdbAuthWebServer.enqueue(
            MockResponse()
                .setResponseCode(HTTP_OK)
                .setBody(createSessionWithLoginResponseBody)
        )
        mockTmdbAuthWebServer.enqueue(
            MockResponse()
                .setResponseCode(HTTP_OK)
                .setBody(createSessionIdFalseResponseBoolean)
        )
        val testScheduler = TestScheduler()
        val testObserver = loginRepository
            .login(User("nekitpip@mail.ru", "q1w2e3r4"))
            .subscribeOn(testScheduler)
            .observeOn(testScheduler)
            .test()

        testScheduler.advanceTimeBy(1L, TimeUnit.SECONDS)
        testObserver.assertValue(false)
        testObserver.dispose()
    }
}
