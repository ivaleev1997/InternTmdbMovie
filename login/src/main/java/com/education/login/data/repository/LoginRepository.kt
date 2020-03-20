package com.education.login.data.repository

import com.education.login.domain.entity.User
import io.reactivex.Completable

interface LoginRepository {
    fun login(user: User): Completable
}