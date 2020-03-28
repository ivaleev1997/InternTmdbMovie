package com.education.login

import com.education.core.*
import com.education.core_api.data.LocalDataSource
import com.education.login.data.repository.LoginRepository
import com.education.login.data.repository.LoginRepositoryImpl
import com.education.login.domain.UserUseCase
import com.education.login.domain.entity.LoginResult
import com.education.login.presentation.LoginViewModel
import io.reactivex.schedulers.Schedulers
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.QueueDispatcher
import org.assertj.core.api.Assertions.assertThat
import org.mockito.Mockito
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import java.net.HttpURLConnection

object LoginJvmIntegrationTest : Spek({
    beforeGroup { enableTestMode() }
    afterGroup { disableTestMode() }

    Feature("Login: ViewModel + UserUseCase + ... + MockWebServer") {
        val testScheduler = Schedulers.trampoline()
        val mockLocalDataSource: LocalDataSource = Mockito.mock(LocalDataSource::class.java)
        val schedulersProvider = TestSchedulersProvider(testScheduler)

        var loginRepository: LoginRepository
        var mockTmdbServer: MockTmdbAuthWebServer
        var userUseCase: UserUseCase
        var loginViewModel: LoginViewModel

        Scenario("User insert valid login and password and click enter button") {
            mockTmdbServer = MockTmdbAuthWebServer()
            loginRepository = LoginRepositoryImpl(mockTmdbServer.tmdbAuthApi, mockLocalDataSource)
            userUseCase = UserUseCase(loginRepository)
            loginViewModel = LoginViewModel(userUseCase, schedulersProvider)
            var login = ""
            var password = ""
            val expectedButtonStatus = true
            val expectedLoginResult = LoginResult.SUCCESS
            Given("Set correct password and set success dispatcher") {
                login = "login"
                password = "password"
                mockTmdbServer.setDispatcher(SuccessDispatcher)
            }

            When("Enter correct login") {
                loginViewModel.onLoginEntered(login)
            }

            And("Enter correct password") {
                loginViewModel.onPasswordEntered(password)
            }

            And("Clicked login button") {
                loginViewModel.onLoginButtonClicked(login, password)
            }

            Then("Button is enabled") {
                assertThat(loginViewModel.validateButtonStatus.value).isEqualTo(expectedButtonStatus)
            }

            And("Login result should be SUCCESS") {
                //testScheduler.triggerActions()
                assertThat(loginViewModel.login.value).isEqualTo(expectedLoginResult)
            }
        }

        Scenario("User insert valid login and password and click enter button but server send 401 error") {
            mockTmdbServer = MockTmdbAuthWebServer()
            loginRepository = LoginRepositoryImpl(mockTmdbServer.tmdbAuthApi, mockLocalDataSource)
            userUseCase = UserUseCase(loginRepository)
            loginViewModel = LoginViewModel(userUseCase, schedulersProvider)

            var login = ""
            var password = ""
            val expectedLoginResult = LoginResult.LOGIN_OR_PASSWORD
            Given("Set correct password and set error unauthorized dispatcher") {
                login = "login"
                password = "password"
                mockTmdbServer.setDispatcher(ErrorUnAuthorizeDispatcher)
            }

            When("Clicked enter button with correct login and password") {
                loginViewModel.onLoginButtonClicked(login, password)
            }

            Then("Login result should be LOGIN_OR_PASSWORD") {
                //testScheduler.triggerActions()
                assertThat(loginViewModel.login.value).isEqualTo(expectedLoginResult)
            }
        }

        Scenario("User insert valid login and password and click enter button but server send 404 error") {
            mockTmdbServer = MockTmdbAuthWebServer()
            loginRepository = LoginRepositoryImpl(mockTmdbServer.tmdbAuthApi, mockLocalDataSource)
            userUseCase = UserUseCase(loginRepository)
            loginViewModel = LoginViewModel(userUseCase, schedulersProvider)

            var login = ""
            var password = ""
            val expectedLoginResult = LoginResult.TRY_LATER
            Given("Set correct password and enqueue HTTP_NOT_FOUND response") {
                login = "login"
                password = "password"
                mockTmdbServer.setDispatcher(QueueDispatcher())
                mockTmdbServer.enqueue(MockResponse().setResponseCode(HttpURLConnection.HTTP_NOT_FOUND))
            }

            When("Clicked enter button with correct login and password") {
                loginViewModel.onLoginButtonClicked(login, password)
            }

            Then("Login result should be LOGIN_OR_PASSWORD") {
                //testScheduler.triggerActions()
                assertThat(loginViewModel.login.value).isEqualTo(expectedLoginResult)
            }
        }

        Scenario("User insert valid login and password and click enter button but server shutdown") {
            mockTmdbServer = MockTmdbAuthWebServer()
            loginRepository = LoginRepositoryImpl(mockTmdbServer.tmdbAuthApi, mockLocalDataSource)
            userUseCase = UserUseCase(loginRepository)
            loginViewModel = LoginViewModel(userUseCase, schedulersProvider)

            var login = ""
            var password = ""
            val expectedLoginResult = LoginResult.NO_NETWORK_CONNECTION
            Given("Set correct password and shutdown server") {
                login = "login"
                password = "password"
                mockTmdbServer.mockWebServer.shutdown()
            }

            When("Clicked enter button with correct login and password") {
                loginViewModel.onLoginButtonClicked(login, password)
            }

            Then("Login result should be LOGIN_OR_PASSWORD") {
                //testScheduler.triggerActions()
                assertThat(loginViewModel.login.value).isEqualTo(expectedLoginResult)
            }
        }
    }
})

