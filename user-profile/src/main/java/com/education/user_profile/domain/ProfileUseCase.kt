package com.education.user_profile.domain

import com.education.user_profile.data.repository.ProfileRepository
import com.education.user_profile.domain.entity.Profile
import io.reactivex.Single
import javax.inject.Inject

class ProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {

    fun loadUserProfile(): Single<Profile> {
        return profileRepository.loadUserAccount().map { Profile(it.name, it.username) }
    }
}