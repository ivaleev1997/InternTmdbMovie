package com.education.login.usecase

import com.education.core_api.dto.User
import com.education.login.repository.LoginRepository

class UserUseCase {
    //DI пока не стал делать
    private val loginRepository = LoginRepository()

    fun login(login: String, passwd: String) {
        loginRepository.login(User(login, passwd))
    }
}