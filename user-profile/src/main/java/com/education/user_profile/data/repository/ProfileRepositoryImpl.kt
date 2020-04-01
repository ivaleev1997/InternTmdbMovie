package com.education.user_profile.data.repository

import com.education.core_api.data.network.TmdbMovieApi
import com.education.core_api.data.network.entity.Account
import io.reactivex.Single
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val tmdbMovieApi: TmdbMovieApi
) : ProfileRepository {

    override fun loadUserAccount(): Single<Account> {
        return tmdbMovieApi.getAccountInfo()
    }
}