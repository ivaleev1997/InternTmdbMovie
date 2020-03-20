package com.education.login.viewmodel

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.education.login.dto.LoginResult
import com.education.login.usecase.UserUseCase
import javax.inject.Inject

class LoginViewModel
@Inject constructor (
    private val userUseCase: UserUseCase
) : ViewModel() {

    companion object {
        const val PASSWORD_MIN = 6
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
    }

    fun onLoginEntered(login: String) {
        isLoginValid(login)
    }

    fun onPasswordEntered(password: String) {
        isPasswordValid(password)
    }

    private fun isLoginValid(login: String): Boolean {
        val result = login.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(login).matches()
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

}
