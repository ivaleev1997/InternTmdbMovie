package com.education.core_impl.data

import android.content.SharedPreferences
import com.education.core_api.*
import com.education.core_api.data.LocalDataSource
import com.education.core_api.extension.putLong
import com.education.core_api.extension.putString
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val sharedPrefs: SharedPreferences
) : LocalDataSource {
    override fun saveSessionId(sessionId: String) {
        sharedPrefs.putString(PREFS_SESSION, sessionId)
    }

    override fun saveRequestToken(token: String) {
        sharedPrefs.putString(PREFS_REQUEST_TOKEN, token)
    }

    override fun saveTokenLifeTime(expiresAt: String) {
        sharedPrefs.putLong(PREFS_REQUEST_LIFE, convertTime(expiresAt))
    }

    override fun getSessionId(): String {
        return sharedPrefs.getString(PREFS_SESSION, BLANK_STR) ?: BLANK_STR
    }

    override fun getRequestToken(): String {
        return sharedPrefs.getString(PREFS_REQUEST_TOKEN, BLANK_STR) ?: BLANK_STR
    }

    override fun getTokenLifeTime(): Long {
        return sharedPrefs.getLong(PREFS_REQUEST_LIFE, 0L)
    }
}