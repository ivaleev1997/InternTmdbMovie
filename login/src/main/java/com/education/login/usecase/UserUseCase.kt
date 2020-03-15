package com.education.login.usecase

import com.education.core_api.dto.User
import com.education.login.repository.LoginRepository
import io.reactivex.Single
import javax.inject.Inject

class UserUseCase
@Inject constructor (
    private val loginRepository: LoginRepository
) {

    fun login(login: String, password: String): Single<Boolean> {
        return loginRepository.login(User(login, password))
    }
}
