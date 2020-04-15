package com.education.core_impl.data

import android.content.SharedPreferences
import com.education.core_api.BLANK_STR
import com.education.core_api.data.LocalDataSource
import com.education.core_api.extension.clear
import com.education.core_api.extension.putLong
import com.education.core_api.extension.putString
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val sharedPrefs: SharedPreferences
) : LocalDataSource {

    companion object {
        const val PREFS_REQUEST_TOKEN = "request_token"
        const val PREFS_REQUEST_LIFE = "request_token_life"
        const val PREFS_SESSION = "session"

        fun convertTime(timeString: String): Long {
            val timeFormat = "yyyy-MM-dd HH:mm:ss"
            return try {
                val sdf = SimpleDateFormat(timeFormat)
                sdf.timeZone = TimeZone.getTimeZone("UTC")

                val sdfOutPutToSend = SimpleDateFormat(timeFormat)
                sdfOutPutToSend.timeZone = TimeZone.getDefault()

                val date = sdf.parse(timeString)
                date?.time ?: 0L
            } catch (e: Exception) {
                0L
            }
        }
    }

    override fun saveSessionId(sessionId: String) {
        sharedPrefs.putString(PREFS_SESSION, sessionId)
    }

    override fun saveRequestToken(token: String) {
        sharedPrefs.putString(PREFS_REQUEST_TOKEN, token)
    }

    override fun saveTokenLifeTime(expiresAt: String) {
        sharedPrefs.putLong(PREFS_REQUEST_LIFE,
            convertTime(expiresAt)
        )
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

    override fun cleanTokens() {
        sharedPrefs.clear()
    }
}
