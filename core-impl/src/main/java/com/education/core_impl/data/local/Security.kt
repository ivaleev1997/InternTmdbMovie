package com.education.core_impl.data.local

import android.content.Context
import com.education.core_api.BLANK_STR
import com.education.core_impl.data.local.encryption.Encryption
import javax.inject.Inject
import javax.inject.Singleton

class Security @Inject constructor(
    private val encryption: Encryption
) {
    @Volatile
    lateinit var pin: String

    fun isLoggedIn() = pin.isNotBlank()

    fun encrypt(data: String): String {
        return if (isLoggedIn())
            encryption.encrypt(pin, data)
        else BLANK_STR
    }

    fun decrypt(data: String): String {
        return if (isLoggedIn())
            encryption.decrypt(pin, data)
        else BLANK_STR
    }

    fun encryptUserName(data: String, context: Context): String {
        return encryption.encrypt(data, context)
    }

    fun decryptUserName(data: String): String {
        return encryption.decrypt(data)
    }
}