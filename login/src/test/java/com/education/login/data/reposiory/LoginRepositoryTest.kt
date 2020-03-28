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
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.Single
import io.reactivex.Single.just
import io.reactivex.observers.TestObserver
import org.mockito.Mockito
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import java.net.ConnectException

object LoginRepositoryTest : Spek({
    // region Fields
    val mockTmdbAuthApi = mock<TmdbAuthApi> {
        on { createRequestToken() } doReturn just(
            RequestToken(success = true, expiresAt = expires_at, requestToken = request_token)
        )

        on { validateRequestTokenWithLogin(any()) } doReturn just(
            RequestToken(success = true, expiresAt = expires_at, requestToken = request_token)
        )
    }
    val mockLocalDataSource: LocalDataSource = Mockito.mock(LocalDataSource::class.java)

    var testObserver: TestObserver<Void> = TestObserver()

    val loginRepository: LoginRepository by memoized {
        LoginRepositoryImpl(mockTmdbAuthApi, mockLocalDataSource)
    }
    // endregion Fields

    Feature("Check login(user: User) method") {

        Scenario("Check login result if create session true") {
            Given("Create success data flow from TmdbAuthApi by mocking") {
                Mockito.`when`(mockTmdbAuthApi.createSessionId(any())).thenReturn(just(
                    Session(success = true, sessionId = session_id)
                ))
            }

            When("Subscribe on login method with User instance object") {
                testObserver = loginRepository
                    .login(User("login", "passwd"))
                    .test()
            }

            Then("Should complete"){
                testObserver.assertComplete()
                testObserver.dispose()
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
                    .login(User("login", "passwd"))
                    .test()
            }

            Then("Should throw SessionTokenException"){
                testObserver.assertError(SessionTokenException::class.java)
                testObserver.dispose()
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
                    .login(User("login", "passwd"))
                    .test()
            }

            Then("Should throw ConnectException") {
                testObserver.assertError(ConnectException::class.java)
                testObserver.dispose()
            }
        }
    }
})
