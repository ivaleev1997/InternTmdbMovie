package com.education.core_impl.data.local

import android.content.Context
import android.content.SharedPreferences
import com.education.core_api.BLANK_STR
import com.education.core_api.data.LocalDataSource
import com.education.core_api.data.local.dao.MovieDao
import com.education.core_api.data.local.entity.Movie
import com.education.core_api.dto.UserCredentials
import com.education.core_api.extension.clear
import com.education.core_api.extension.getStringOrBlank
import com.education.core_api.extension.putString
import io.reactivex.Completable
import io.reactivex.Single
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val movieDao: MovieDao,
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
        const val PREFS_MASTER_KEY_PIN = "masterPinKey"
        const val PREFS_MAP_RECYCLER_STATE = "recyclerMapState"

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

    override fun setMasterPinKey(pin: String) {
        security.pin = pin
    }

    override fun saveMasterPinKey(pinKey: String): Boolean {
        return sharedPrefs.putString(PREFS_MASTER_KEY_PIN, pinKey)
    }

    override fun getMasterPinKey(): String {
        return sharedPrefs.getStringOrBlank(PREFS_MASTER_KEY_PIN, BLANK_STR)
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
        return security.decrypt(sharedPrefs.getStringOrBlank(PREFS_SESSION, BLANK_STR))
    }

    override fun getRequestToken(): String {
        return security.decrypt(sharedPrefs.getStringOrBlank(PREFS_REQUEST_TOKEN, BLANK_STR))
    }

    override fun getTokenLifeTime(): Long {
        val decrypted = security.decrypt(sharedPrefs.getStringOrBlank(PREFS_REQUEST_LIFE, BLANK_STR))

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
        return security.decrypt(sharedPrefs.getStringOrBlank(PREFS_USER_LOGIN, BLANK_STR))
    }

    override fun saveUserPassword(userPass: String): Boolean {
        val encrypted = security.encrypt(userPass)

        return encrypted.isNotEmpty() && sharedPrefs.putString(PREFS_USER_PASSWORD, encrypted)
    }

    override fun getUserPassword(): String {
        return security.decrypt(sharedPrefs.getStringOrBlank(PREFS_USER_PASSWORD, BLANK_STR))
    }

    override fun saveUserName(userName: String, context: Context): Boolean {
        val encrypted = security.encryptOnKeysStore(userName)

        return encrypted.isNotEmpty() && sharedPrefs.putString(PREFS_USER_NAME, encrypted)
    }

    override fun getUserName(): String {
        return security.decryptOnKeyStore(sharedPrefs.getStringOrBlank(PREFS_USER_NAME, BLANK_STR))
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
        val decrypted = security.decryptOnKeyStore(sharedPrefs.getStringOrBlank(PREFS_ON_STOP_TIME, BLANK_STR))

        return decrypted.toLongOrNull() ?: 0L
    }

    override fun clearLastOnStopTime() {
        sharedPrefs.putString(PREFS_ON_STOP_TIME, BLANK_STR)
    }

    override fun saveMovies(listMovies: List<Movie>): Completable {
        return movieDao.insertMovies(listMovies)
    }

    override fun getMovies(): Single<List<Movie>> {
        return movieDao.getMovies()
    }

    override fun saveRecyclerMapState(flag: Boolean) {
        sharedPrefs.putString(PREFS_MAP_RECYCLER_STATE, security.encrypt(flag.toString()))
    }

    override fun getRecyclerMapState(): Boolean {
        val decrypted =
            security.decrypt(sharedPrefs.getStringOrBlank(PREFS_MAP_RECYCLER_STATE, BLANK_STR))

        return if (decrypted.isNotBlank())
                    decrypted.toBoolean()
                else false
    }
}
