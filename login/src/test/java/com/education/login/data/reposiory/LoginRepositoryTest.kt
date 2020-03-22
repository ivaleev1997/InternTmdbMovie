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
        val mockTmdbAuthApi: TmdbAuthApi = Mockito.mock(TmdbAuthApi::class.java).also {
            Mockito.`when`(it.createRequestToken()).thenReturn(just(
                RequestToken(success = true, expiresAt = expires_at, requestToken = request_token)
            ))
            Mockito.`when`(it.validateRequestTokenWithLogin(any())).thenReturn(just(
                RequestToken(success = true, expiresAt = expires_at, requestToken = request_token)
            ))
        }
        val mockLocalDataSource: LocalDataSource = Mockito.mock(LocalDataSource::class.java)

        Scenario("Check login result if create session true") {

            var loginRepository: LoginRepository? = null

            var testObserver: TestObserver<Void>? = null

            Given("Create success data flow from TmdbAuthApi by mocking") {
                Mockito.`when`(mockTmdbAuthApi.createSessionId(any())).thenReturn(just(
                    Session(success = true, sessionId = session_id)
                ))


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
            var loginRepository: LoginRepository? = null

            var testObserver: TestObserver<Void>? = null

            Given("Create data flow from TmdbAuthApi by mocking with false session") {
                Mockito.`when`(mockTmdbAuthApi.createSessionId(any())).thenReturn(just(
                    Session(success = false, sessionId = "")
                ))

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
