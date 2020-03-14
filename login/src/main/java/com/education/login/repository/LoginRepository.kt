package com.education.login.repository

import com.education.core_api.dto.User
import com.education.core_api.network.TmdbMovieApi
import javax.inject.Inject

class LoginRepository
@Inject constructor(
    private val tmdbApi: TmdbMovieApi
) {

    fun login(user: User) {
    }
}
