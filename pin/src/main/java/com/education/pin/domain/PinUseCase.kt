package com.education.pin.domain

import android.content.Context
import com.education.core_api.dto.UserCredentials
import com.education.pin.data.PinRepository
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class PinUseCase @Inject constructor(
    private val pinRepository: PinRepository
) {
    fun saveUserCredentials(userCredentials: UserCredentials, pin: String): Completable {
        return pinRepository.saveCredentials(userCredentials, pin)
    }

    fun saveUserName(context: Context): Completable {
        return pinRepository.fetchAndSaveUserName(context)
    }

    fun getUserName(): Single<String> {
        return pinRepository.getUserName()
    }

    fun getUserLogin(pin: String): Single<String> {
        return pinRepository.getUserLogin(pin)
    }
}