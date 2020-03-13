package com.education.login.usecase

import com.education.core_api.dto.User
import com.education.login.repository.LoginRepository
import javax.inject.Inject

class UserUseCase
@Inject constructor (
    private val loginRepository: LoginRepository
) {

    fun login(login: String, password: String) {
        loginRepository.login(User(login, password))
    }
}
