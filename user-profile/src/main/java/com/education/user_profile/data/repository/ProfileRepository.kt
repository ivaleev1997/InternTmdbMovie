package com.education.user_profile.data.repository

import com.education.core_api.data.network.entity.Account
import io.reactivex.Single

interface ProfileRepository {

    fun loadUserAccount(): Single<Account>
}