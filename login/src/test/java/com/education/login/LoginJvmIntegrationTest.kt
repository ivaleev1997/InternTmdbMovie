package com.education.login

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import com.education.core.ErrorUnAuthorizeDispatcher
import com.education.core.MockTmdbAuthWebServer
import com.education.core.SuccessDispatcher
import com.education.core_api.data.LocalDataSource
import com.education.core_api.extension.SchedulersProvider
import com.education.login.data.repository.LoginRepository
import com.education.login.data.repository.LoginRepositoryImpl
import com.education.login.domain.UserUseCase
import com.education.login.domain.entity.LoginResult
import com.education.login.presentation.LoginViewModel
import io.reactivex.Scheduler
import io.reactivex.schedulers.TestScheduler
import org.assertj.core.api.Assertions.assertThat
import org.mockito.Mockito
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object LoginJvmIntegrationTest : Spek({
    beforeGroup { enableTestMode() }
    afterGroup { disableTestMode() }

    Feature("Login: ViewModel only (login and password input only)") {
        val mockLoginRepository = Mockito.mock(LoginRepository::class.java)
        val mockScheduler = Mockito.mock(SchedulersProvider::class.java)

        val userUseCase = UserUseCase(mockLoginRepository, mockScheduler)
        val loginViewModel = LoginViewModel(userUseCase)

        Scenario("User insert wrong password") {
            var password = ""
            val expectedValidateStatus = false
            Given("Set wrong password") {
                password = "pas"
            }

            When("Validate") {
                loginViewModel.onPasswordEntered(password)
            }

            Then("Password status result should be false") {
                assertThat(loginViewModel.validatePasswordStatus.value).isEqualTo(expectedValidateStatus)
            }
        }

        Scenario("User insert empty login") {
            var login = ""
            val expectedValidateStatus = false
            Given("Set empty login") {
                login = ""
            }

            When("Validate login") {
                loginViewModel.onLoginEntered(login)
            }

            Then("Password status result should be false") {
                assertThat(loginViewModel.validateLoginStatus.value).isEqualTo(expectedValidateStatus)
            }
        }

        Scenario("User insert empty password") {
            val password = ""
            val expectedValidateStatus = false

            When("Validate login") {
                loginViewModel.onPasswordEntered(password)
            }

            Then("Password status result should be false") {
                assertThat(loginViewModel.validatePasswordStatus.value).isEqualTo(expectedValidateStatus)
            }
        }

        Scenario("User insert valid login and password") {
            var password = ""
            var login = ""
            val expectedPasswordValidateStatus = true
            val expectedLoginValidateStatus = true
            val expectedButtonValidateStatus = true

            Given("Set valid login and password") {
                password = "password"
                login = "login"
            }

            When("Validate login") {
                loginViewModel.onPasswordEntered(password)
                loginViewModel.onLoginEntered(login)
            }

            Then("Password and login status result should be true") {
                assertThat(loginViewModel.validatePasswordStatus.value).isEqualTo(expectedPasswordValidateStatus)
                assertThat(loginViewModel.validateLoginStatus.value).isEqualTo(expectedLoginValidateStatus)
            }
            And("Button is enabled - button status is true") {
                assertThat(loginViewModel.validateButtonStatus.value).isEqualTo(expectedButtonValidateStatus)
            }
        }
    }

    Feature("Login: ViewModel + UserUseCase + ... + MockWebServer") {
        val mockTmdbServer = MockTmdbAuthWebServer()
        val testScheduler = TestScheduler()
        val mockLocalDataSource: LocalDataSource = Mockito.mock(LocalDataSource::class.java)
        //var testObserver: TestObserver<Void>? = null
        val loginRepository: LoginRepository = LoginRepositoryImpl(mockTmdbServer.tmdbAuthApi, mockLocalDataSource)
        val schedulersProvider = object : SchedulersProvider {
            override fun io(): Scheduler = testScheduler
            override fun mainThread(): Scheduler = testScheduler
            override fun computation(): Scheduler = testScheduler
        }
        val userUseCase = UserUseCase(loginRepository, schedulersProvider)
        val loginViewModel = LoginViewModel(userUseCase)

        Scenario("User insert valid login and password and click enter button") {
            var login = ""
            var password = ""
            val expectedButtonStatus = true
            val expectedLoginResult = LoginResult.SUCCESS
            Given("Set correct password and set success dispatcher") {
                login = "login"
                password = "password"
                mockTmdbServer.setDispatcher(SuccessDispatcher)
            }

            When("Enter correct login and password") {
                loginViewModel.onLoginEntered(login)
                loginViewModel.onPasswordEntered(password)
            }

            And("Clicked login button") {
                loginViewModel.onLoginClicked(login, password)
            }

            Then("Button is enabled") {
                assertThat(loginViewModel.validateButtonStatus.value).isEqualTo(expectedButtonStatus)
            }

            And("Login result should be SUCCESS") {
                testScheduler.triggerActions()
                assertThat(loginViewModel.login.value).isEqualTo(expectedLoginResult)
            }
        }

        Scenario("User insert valid login and password and click enter button but server send 401 error") {
            var login = ""
            var password = ""
            val expectedLoginResult = LoginResult.LOGIN_OR_PASSWORD
            Given("Set correct password and set error unauthorized dispatcher") {
                login = "login"
                password = "password"
                mockTmdbServer.setDispatcher(ErrorUnAuthorizeDispatcher)
            }

            When("Clicked enter button with correct login and password") {
                loginViewModel.onLoginClicked(login, password)
            }

            Then("Login result should be LOGIN_OR_PASSWORD") {
                testScheduler.triggerActions()
                assertThat(loginViewModel.login.value).isEqualTo(expectedLoginResult)
            }
        }
    }
})

private fun enableTestMode() {
    ArchTaskExecutor.getInstance()
        .setDelegate(object : TaskExecutor() {
            override fun executeOnDiskIO(runnable: Runnable) = runnable.run()
            override fun postToMainThread(runnable: Runnable) = runnable.run()
            override fun isMainThread(): Boolean = true
        })
}

private fun disableTestMode() {
    ArchTaskExecutor.getInstance().setDelegate(null)
}

