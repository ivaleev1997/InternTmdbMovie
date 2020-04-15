package com.education.core_impl.data.local

import android.content.Context
import android.content.SharedPreferences
import com.education.core_api.BLANK_STR
import com.education.core_api.data.LocalDataSource
import com.education.core_api.dto.UserCredentials
import com.education.core_api.extension.clear
import com.education.core_api.extension.putString
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val sharedPrefs: SharedPreferences,
    private val security: Security
) : LocalDataSource {

    companion object {
        const val PREFS_REQUEST_TOKEN = "request_token"
        const val PREFS_REQUEST_LIFE = "request_token_life"
        const val PREFS_SESSION = "session"
        const val PREFS_USER_LOGIN = "userLog"
        const val PREFS_USER_NAME = "userName"
        const val PREFS_USER_PASSWORD = "userPassword"
        const val PREFS_ON_STOP_TIME = "onStopTime"

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

    override fun setMasterKey(pin: String) {
        security.pin = pin
    }

    override fun saveSessionId(sessionId: String): Boolean {
        val encrypted = security.encrypt(sessionId)

        return encrypted.isNotEmpty() && sharedPrefs.putString(PREFS_SESSION, encrypted)
    }

    override fun saveRequestToken(token: String): Boolean {
        val encrypted = security.encrypt(token)

        return encrypted.isNotEmpty() && sharedPrefs.putString(PREFS_REQUEST_TOKEN, encrypted)
    }

    override fun saveTokenLifeTime(expiresAt: String): Boolean {
        val encrypted = security.encrypt(
            convertTime(expiresAt).toString()
        )

        return encrypted.isNotEmpty() && sharedPrefs.putString(PREFS_REQUEST_LIFE, encrypted)
    }

    override fun getSessionId(): String {
        return security.decrypt(sharedPrefs.getString(PREFS_SESSION, BLANK_STR) ?: BLANK_STR)
    }

    override fun getRequestToken(): String {
        return security.decrypt(sharedPrefs.getString(PREFS_REQUEST_TOKEN, BLANK_STR) ?: BLANK_STR)
    }

    override fun getTokenLifeTime(): Long {
        val decrypted = security.decrypt(sharedPrefs.getString(PREFS_REQUEST_LIFE, BLANK_STR) ?: BLANK_STR)

        return decrypted.toLongOrNull() ?: 0L
    }

    override fun cleanTokens() {
        sharedPrefs.clear()
    }

    override fun saveUserLogin(userLogin: String): Boolean {
        val encrypted = security.encrypt(userLogin)

        return encrypted.isNotEmpty() && sharedPrefs.putString(PREFS_USER_LOGIN, encrypted)
    }

    override fun getUserLogin(): String {
        return security.decrypt(sharedPrefs.getString(PREFS_USER_LOGIN, BLANK_STR) ?: BLANK_STR)

    }

    override fun saveUserPassword(userPass: String): Boolean {
        val encrypted = security.encrypt(userPass)

        return encrypted.isNotEmpty() && sharedPrefs.putString(PREFS_USER_PASSWORD, encrypted)
    }

    override fun getUserPassword(): String {
        return security.decrypt(sharedPrefs.getString(PREFS_USER_PASSWORD, BLANK_STR) ?: BLANK_STR)
    }

    override fun saveUserName(userName: String, context: Context): Boolean {
        val encrypted = security.encryptOnKeysStore(userName)

        return encrypted.isNotEmpty() && sharedPrefs.putString(PREFS_USER_NAME, encrypted)
    }

    override fun getUserName(): String {
        return security.decryptOnKeyStore(sharedPrefs.getString(PREFS_USER_NAME, BLANK_STR) ?: BLANK_STR)
    }

    override fun saveUserCredentials(userCredentials: UserCredentials): Boolean {
        return saveUserLogin(userCredentials.userLogin) &&
                saveUserPassword(userCredentials.userPassword) &&
                saveRequestToken(userCredentials.requestToken) &&
                saveSessionId(userCredentials.sessionId) &&
                saveTokenLifeTime(userCredentials.requestTokenLife)
    }

    override fun saveOnStopTime(timeMills: Long): Boolean {
        val encrypted = security.encryptOnKeysStore(timeMills.toString())

        return encrypted.isNotEmpty() && sharedPrefs.putString(PREFS_ON_STOP_TIME, encrypted)
    }

    override fun getOnStopTime(): Long {
        val decrypted = security.decryptOnKeyStore(sharedPrefs.getString(PREFS_ON_STOP_TIME, BLANK_STR) ?: BLANK_STR)

        return decrypted.toLongOrNull() ?: 0L
    }

    override fun clearLastOnStopTime() {
        sharedPrefs.putString(PREFS_ON_STOP_TIME, "")
    }
}
