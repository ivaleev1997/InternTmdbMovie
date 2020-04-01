package com.education.core_api.data

interface LocalDataSource {

    fun saveSessionId(sessionId: String)

    fun saveRequestToken(token: String)

    fun saveTokenLifeTime(expiresAt: String)

    fun getSessionId(): String

    fun getRequestToken(): String

    fun getTokenLifeTime(): Long

    fun cleanTokens()
}