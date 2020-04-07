package com.education.login.presentation

import com.education.core_api.extension.SchedulersProvider
import com.education.login.domain.UserUseCase
import com.nhaarman.mockitokotlin2.mock
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object LoginViewModelTest : Spek({
    Feature("Login: ViewModel only (login and password input only)") {
        val mockUserUseCase = mock<UserUseCase>()
        val mockSchedulersProvider = mock<SchedulersProvider>()
        val loginViewModel = LoginViewModel(mockUserUseCase, mockSchedulersProvider)

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
                Assertions.assertThat(loginViewModel.validatePasswordStatus.value).isEqualTo(expectedValidateStatus)
            }
        }

        Scenario("User insert empty_animation login") {
            var login = ""
            val expectedValidateStatus = false
            Given("Set empty_animation login") {
                login = ""
            }

            When("Validate login") {
                loginViewModel.onLoginEntered(login)
            }

            Then("Password status result should be false") {
                Assertions.assertThat(loginViewModel.validateLoginStatus.value).isEqualTo(expectedValidateStatus)
            }
        }

        Scenario("User insert empty_animation password") {
            val password = ""
            val expectedValidateStatus = false

            When("Validate login") {
                loginViewModel.onPasswordEntered(password)
            }

            Then("Password status result should be false") {
                Assertions.assertThat(loginViewModel.validatePasswordStatus.value).isEqualTo(expectedValidateStatus)
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
                assertSoftly {
                    assertThat(loginViewModel.validatePasswordStatus.value).isEqualTo(expectedPasswordValidateStatus)
                    assertThat(loginViewModel.validateLoginStatus.value).isEqualTo(expectedLoginValidateStatus)
                }
            }

            And("Button is enabled - button status is true") {
                assertThat(loginViewModel.validateButtonStatus.value).isEqualTo(expectedButtonValidateStatus)
            }
        }
    }
})
