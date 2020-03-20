package com.education.login.domain

import com.education.login.domain.entity.User
import com.education.login.data.repository.LoginRepository
import io.reactivex.Completable
import javax.inject.Inject

class UserUseCase
@Inject constructor (
    private val loginRepository: LoginRepository
) {

    fun login(login: String, password: String): Completable {
        return loginRepository.login(
            User(
                login,
                password
            )
        )
    }
}
