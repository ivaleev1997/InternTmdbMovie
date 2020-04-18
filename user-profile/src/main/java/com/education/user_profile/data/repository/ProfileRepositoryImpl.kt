package com.education.user_profile.data.repository

import com.education.core_api.data.LocalDataSource
import io.reactivex.Single
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource
) : ProfileRepository {

    override fun loadUserAccount(): Single<String> {
        return Single.just(localDataSource.getUserName())
    }
}
