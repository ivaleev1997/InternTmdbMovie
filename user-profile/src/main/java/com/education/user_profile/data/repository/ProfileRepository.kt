package com.education.user_profile.data.repository

import io.reactivex.Single

interface ProfileRepository {
    fun loadUserAccount(): Single<String>
}
