package com.education.login.data.reposiory

import com.education.core_api.data.LocalDataSource
import com.education.core_api.data.network.TmdbAuthApi
import com.education.core_api.data.network.entity.RequestToken
import com.education.core_api.data.network.entity.Session
import com.education.core_api.data.network.exception.SessionTokenException
import com.education.login.data.repository.LoginRepository
import com.education.login.data.repository.LoginRepositoryImpl
import com.education.login.domain.entity.User
import com.education.login.expires_at
import com.education.login.request_token
import com.education.login.session_id
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.Single.just
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.TestScheduler
import org.mockito.Mockito
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import java.util.concurrent.TimeUnit

object LoginRepositoryTest : Spek({
    Feature("Check login(user: User) method") {
        val testScheduler = TestScheduler()
        Scenario("Check login result if create session true") {

            var mockTmdbAuthApi: TmdbAuthApi

            var mockLocalDataSource: LocalDataSource

            var loginRepository: LoginRepository? = null

            var testObserver: TestObserver<Void>? = null

            Given("Create success data flow from TmdbAuthApi by mocking") {
                mockTmdbAuthApi = mock {
                    on { createRequestToken() } doReturn(just(
                        RequestToken(success = true, expiresAt = expires_at, requestToken = request_token)
                    ))

                    on { validateRequestTokenWithLogin(any()) } doReturn(just(
                        RequestToken(success = true, expiresAt = expires_at, requestToken = request_token)
                    ))

                    on { createSessionId(any()) } doReturn(just(
                        Session(success = true, sessionId = session_id)
                    ))
                }

                mockLocalDataSource = Mockito.mock(LocalDataSource::class.java)

                loginRepository = LoginRepositoryImpl(mockTmdbAuthApi, mockLocalDataSource)
            }

            When("Subscribe on login method with User instance object") {
                testObserver = loginRepository
                    ?.login(User("login", "passwd"))
                    ?.subscribeOn(testScheduler)
                    ?.observeOn(testScheduler)
                    ?.test()
            }

            Then("should complete"){
                testScheduler.advanceTimeBy(1L, TimeUnit.SECONDS)
                testObserver?.assertComplete()
                testObserver?.dispose()
            }
        }

        Scenario("Check login result if create session false") {

            var mockTmdbAuthApi: TmdbAuthApi

            var mockLocalDataSource: LocalDataSource

            var loginRepository: LoginRepository? = null

            var testObserver: TestObserver<Void>? = null

            Given("Create data flow from TmdbAuthApi by mocking with false session") {
                mockTmdbAuthApi = mock {
                    on { createRequestToken() } doReturn(just(
                        RequestToken(success = true, expiresAt = expires_at, requestToken = request_token)
                    ))

                    on { validateRequestTokenWithLogin(any()) } doReturn(just(
                        RequestToken(success = true, expiresAt = expires_at, requestToken = request_token)
                    ))

                    on { createSessionId(any()) } doReturn(just(
                        Session(success = false, sessionId = session_id)
                    ))
                }

                mockLocalDataSource = Mockito.mock(LocalDataSource::class.java)

                loginRepository = LoginRepositoryImpl(mockTmdbAuthApi, mockLocalDataSource)
            }

            When("Subscribe on login method with User instance object") {
                testObserver = loginRepository
                    ?.login(User("login", "passwd"))
                    ?.subscribeOn(testScheduler)
                    ?.observeOn(testScheduler)
                    ?.test()
            }

            Then("should throw SessionTokenException"){
                testScheduler.advanceTimeBy(1L, TimeUnit.SECONDS)
                testObserver?.assertError(SessionTokenException::class.java)
                testObserver?.dispose()
            }
        }
    }
})
