package com.education.core_impl.data.local.encryption

interface Encryption {

    fun encrypt(password: String, data: String): String

    fun decrypt(password: String, data: String): String

    fun encrypt(data: String): String

    fun decrypt(data: String): String
}