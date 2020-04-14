package com.education.core_impl.data.local.encryption

import android.content.Context

interface Encryption {

    fun encrypt(password: String, data: String): String

    fun decrypt(password: String, data: String): String

    fun encrypt(data: String, context: Context): String

    fun decrypt(data: String): String
}