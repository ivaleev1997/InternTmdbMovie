package com.education.login.repository

import com.education.core_api.dto.User
import io.reactivex.Completable

interface LoginRepository {
    fun login(user: User): Completable
}