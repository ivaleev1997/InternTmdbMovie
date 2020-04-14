package com.education.pin.data

import android.content.Context
import com.education.core_api.dto.UserCredentials
import io.reactivex.Completable
import io.reactivex.Single

interface PinRepository {
    fun saveCredentials(userCredentials: UserCredentials, pin: String): Completable

    fun fetchAndSaveUserName(context: Context): Completable

    fun getUserName(): Single<String>

    fun getUserLogin(pin: String): Single<String>
}