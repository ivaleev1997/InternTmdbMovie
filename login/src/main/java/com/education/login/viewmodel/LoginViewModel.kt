package com.education.login.viewmodel

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.education.login.dto.LoginResult
import com.education.login.dto.ValidationStatus
import com.education.login.usecase.UserUseCase

class LoginViewModel : ViewModel() {

    companion object {
        const val PASSWORD_MIN = 6
    }
    //DI пока не стал делать
    private val userUseCase = UserUseCase()

    private var currentLoginValidation = false
    private var currentPasswdValidation = false

    private val _validateStatus = MutableLiveData<Pair<ValidationStatus, Boolean>>()
    val validateStatus: LiveData<Pair<ValidationStatus, Boolean>>
        get() = _validateStatus

    private val _loginStatus = MutableLiveData<LoginResult>()
    val login: LiveData<LoginResult>
        get() = _loginStatus

    fun login(login: String, passwd: String) {
        userUseCase.login(login, passwd)
        //_loginStatus.value = LoginResult.TRY_LATER
    }

    fun isLoginValid(login: String) {
        val result = login.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(login).matches()
        handleValidation(result, ValidationStatus.LOGIN)
    }

    fun isPasswdValid(passwd: String) {
        val result = passwd.isNotBlank() && passwd.length >= PASSWORD_MIN
        handleValidation(result, ValidationStatus.PASSWD)
    }

    private fun handleValidation(flag: Boolean, status: ValidationStatus) {
        setValidationValue(flag, status)
        when(status) {
            ValidationStatus.PASSWD -> {
                currentPasswdValidation = flag
                checkAndSetValueIfAllSuccess()
            }
            ValidationStatus.LOGIN -> {
                currentLoginValidation = flag
                checkAndSetValueIfAllSuccess()
            }
            else -> {}
        }
    }

    private fun checkAndSetValueIfAllSuccess() {
        if (currentLoginValidation && currentPasswdValidation)
            setValidationValue(true, ValidationStatus.SUCCESS)
    }

    private fun setValidationValue(flag: Boolean, status: ValidationStatus) {
        _validateStatus.value = Pair(status, flag)
    }
}
