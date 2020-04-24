package com.education.login.domain

import com.education.login.data.repository.LoginRepository
import com.education.login.domain.entity.User
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

    fun getRequestToken(): String {
        return loginRepository.getRequestToken()
    }
    fun getSessionId(): String {
        return loginRepository.getSessionId()
    }
    fun getRequestTokenLifeTime(): String {
        return loginRepository.getRequestTokenLifeTime()
    }
}
