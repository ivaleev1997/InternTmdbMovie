package com.education.login.repository

import android.content.SharedPreferences
import com.education.core_api.PREFS_REQUEST_LIFE
import com.education.core_api.PREFS_REQUEST_TOKEN
import com.education.core_api.PREFS_SESSION
import com.education.core_api.dto.RequestSessionBody
import com.education.core_api.dto.RequestToken
import com.education.core_api.dto.RequestTokenBody
import com.education.core_api.dto.User
import com.education.core_api.extension.putLong
import com.education.core_api.extension.putString
import com.education.core_api.network.TmdbAuthApi
import com.education.core_api.network.exception.SessionTokenException
import io.reactivex.Completable
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class LoginRepositoryImpl
@Inject constructor(
    private val tmdbAuthApi: TmdbAuthApi,
    private val sharedPrefs: SharedPreferences
) : LoginRepository {

    override fun login(user: User): Completable {
        return tmdbAuthApi.createRequestToken()
            .flatMap { requestToken: RequestToken ->
                saveRequestToken(requestToken.requestToken)
                saveTokenLifeTime(requestToken.expiresAt)
                tmdbAuthApi.validateRequestTokenWithLogin(
                    RequestTokenBody(
                        user.login,
                        user.passwd,
                        requestToken.requestToken
                    )
                ).flatMap { validatedRequestToken ->
                    tmdbAuthApi.createSessionId(RequestSessionBody(validatedRequestToken.requestToken))
                }
            }
            .doOnSuccess { session ->
                if (session.success)
                    saveSessionId(session.sessionId)
                else
                    throw SessionTokenException()
            }
            .toCompletable()
    }

    private fun saveRequestToken(token: String) {
        sharedPrefs.putString(PREFS_REQUEST_TOKEN, token)
    }

    private fun saveSessionId(sessionId: String) {
        sharedPrefs.putString(PREFS_SESSION, sessionId)
    }

    private fun saveTokenLifeTime(expiresAt: String) {
        sharedPrefs.putLong(PREFS_REQUEST_LIFE, convertTime(expiresAt))
    }

    private fun convertTime(timeString: String): Long {
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
