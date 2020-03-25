package com.education.login.data.reposiory

import com.education.core.expires_at
import com.education.core.request_token
import com.education.core.session_id
import com.education.core_api.data.LocalDataSource
import com.education.core_api.data.network.TmdbAuthApi
import com.education.core_api.data.network.entity.RequestToken
import com.education.core_api.data.network.entity.Session
import com.education.core_api.data.network.exception.SessionTokenException
import com.education.login.data.repository.LoginRepository
import com.education.login.data.repository.LoginRepositoryImpl
import com.education.login.domain.entity.User
import com.nhaarman.mockitokotlin2.any
import io.reactivex.Single
import io.reactivex.Single.just
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.TestScheduler
import org.mockito.Mockito
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import java.net.ConnectException
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

        var testObserver: TestObserver<Void>? = null
        val loginRepository: LoginRepository? = LoginRepositoryImpl(mockTmdbAuthApi, mockLocalDataSource)

        Scenario("Check login result if create session true") {
            Given("Create success data flow from TmdbAuthApi by mocking") {
                Mockito.`when`(mockTmdbAuthApi.createSessionId(any())).thenReturn(just(
                    Session(success = true, sessionId = session_id)
                ))
            }

            When("Subscribe on login method with User instance object") {
                testObserver = loginRepository
                    ?.login(User("login", "passwd"))
                    ?.subscribeOn(testScheduler)
                    ?.observeOn(testScheduler)
                    ?.test()
            }

            Then("Should complete"){
                testScheduler.advanceTimeBy(1L, TimeUnit.SECONDS)
                testObserver?.assertComplete()
                testObserver?.dispose()
            }

            And("Verify save request token, session id and token lifetime") {
                Mockito.verify(mockLocalDataSource).saveRequestToken(request_token)
                Mockito.verify(mockLocalDataSource).saveSessionId(session_id)
                Mockito.verify(mockLocalDataSource).saveTokenLifeTime(expires_at)
            }
        }

        Scenario("Check login result if create session false") {
            Given("Create data flow from TmdbAuthApi by mocking with false session") {
                Mockito.`when`(mockTmdbAuthApi.createSessionId(any())).thenReturn(just(
                    Session(success = false, sessionId = "")
                ))
            }

            When("Subscribe on login method with User instance object") {
                testObserver = loginRepository
                    ?.login(User("login", "passwd"))
                    ?.subscribeOn(testScheduler)
                    ?.observeOn(testScheduler)
                    ?.test()
            }

            Then("Should throw SessionTokenException"){
                testScheduler.advanceTimeBy(1L, TimeUnit.SECONDS)
                testObserver?.assertError(SessionTokenException::class.java)
                testObserver?.dispose()
            }
        }

        Scenario("When create session id throw ConnectException in login") {
           Given("Mock 'createSessionId' method and instantiate loginRepository") {
               Mockito.`when`(mockTmdbAuthApi.createSessionId(any())).thenReturn(
                   Single.error(ConnectException())
               )
           }

            When("Subscribe on login method with User instance object") {
                testObserver = loginRepository
                    ?.login(User("login", "passwd"))
                    ?.subscribeOn(testScheduler)
                    ?.observeOn(testScheduler)
                    ?.test()
            }

            Then("Should throw ConnectException") {
                testScheduler.advanceTimeBy(1L, TimeUnit.SECONDS)
                testObserver?.assertError(ConnectException::class.java)
                testObserver?.dispose()
            }
        }
    }
})
