package com.education.login.domain

import com.education.core_api.extension.SchedulersProvider
import com.education.core_api.extension.schedulersIoToMain
import com.education.login.data.repository.LoginRepository
import com.education.login.domain.entity.User
import io.reactivex.Completable
import javax.inject.Inject

class UserUseCase
@Inject constructor (
    private val loginRepository: LoginRepository,
    private val schedulersProvider: SchedulersProvider
) {

    fun login(login: String, password: String): Completable {
        return loginRepository.login(
            User(
                login,
                password
            )
        ).schedulersIoToMain(schedulersProvider)
    }
}
