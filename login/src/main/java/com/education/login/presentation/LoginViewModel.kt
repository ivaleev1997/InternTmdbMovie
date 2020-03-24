package com.education.login.presentation

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.education.core_api.data.network.exception.UnAuthorizedException
import com.education.core_api.extension.isNetworkException
import com.education.core_api.presentation.viewmodel.BaseViewModel
import com.education.login.domain.UserUseCase
import com.education.login.domain.entity.LoginResult
import timber.log.Timber
import javax.inject.Inject

class LoginViewModel
@Inject constructor (
    private val userUseCase: UserUseCase
) : BaseViewModel() {

    companion object {
        const val PASSWORD_MIN = 4
    }

    private val _loginStatus = MutableLiveData<LoginResult>()
    private val _validateButtonStatus = MediatorLiveData<Boolean>()
    private val _validateLoginStatus = MutableLiveData<Boolean>()
    private val _validatePasswordStatus = MutableLiveData<Boolean>()

    val validateButtonStatus: LiveData<Boolean>
        get() = _validateButtonStatus

    val login: LiveData<LoginResult>
        get() = _loginStatus

    val validateLoginStatus: LiveData<Boolean>
        get() = _validateLoginStatus

    val validatePasswordStatus: LiveData<Boolean>
        get() = _validatePasswordStatus

    fun onLoginClicked(login: String, password: String) {
        if (isLoginValid(login) && isPasswordValid(password))
            userUseCase.login(login, password)
                .subscribe({
                    _loginStatus.value = LoginResult.SUCCESS
                }, { error ->
                    _loginStatus.value =
                        when {
                            error.isNetworkException() -> {
                                logThrow(error)
                                LoginResult.NO_NETWORK_CONNECTION
                            }
                            error is UnAuthorizedException -> {
                                logThrow(error)
                                LoginResult.LOGIN_OR_PASSWORD
                            }
                            else -> {
                                logThrow(error)
                                LoginResult.TRY_LATER
                            }
                        }
                    }
                )
                .autoDispose()
    }

    fun onLoginEntered(login: String) {
        isLoginValid(login)
    }

    fun onPasswordEntered(password: String) {
        isPasswordValid(password)
    }

    // Логин может быть любым, поэтому не нужно проверять Patterns.EMAIL_ADDRESS
    private fun isLoginValid(login: String): Boolean {
        Patterns.EMAIL_ADDRESS
        val result = login.isNotBlank()
        _validateLoginStatus.value = result
        checkIfAllSuccess()

        return result
    }

    private fun isPasswordValid(password: String): Boolean {
        val result = password.isNotBlank() && password.length >= PASSWORD_MIN
        _validatePasswordStatus.value = result
        checkIfAllSuccess()

        return result
    }

    private fun checkIfAllSuccess() {
        _validateButtonStatus.value = _validateLoginStatus.value ?: false && _validatePasswordStatus.value ?: false
    }

    private fun logThrow(error: Throwable) {
        Timber.d(error)
    }
}
