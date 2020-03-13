package com.education.login.repository

import com.education.core_api.dto.User
import com.education.core_api.network.TMDBApi
import javax.inject.Inject

class LoginRepository
@Inject constructor(
    private val tmdbApi: TMDBApi
) {

    fun login(user: User) {
    }
}
