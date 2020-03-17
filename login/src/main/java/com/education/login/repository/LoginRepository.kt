package com.education.login.repository

import android.content.SharedPreferences
import com.education.core_api.REQUEST_LIFE
import com.education.core_api.REQUEST_TOKEN
import com.education.core_api.SESSION
import com.education.core_api.dto.*
import com.education.core_api.network.TmdbAuthApi
import io.reactivex.Single
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class LoginRepository
@Inject constructor(
    private val tmdbAuthApi: TmdbAuthApi,
    private val sharedPrefs: SharedPreferences
) {

    fun login(user: User): Single<Boolean> {
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
            .map { session ->
                if (session.success) saveSessionId(session)
                session.success
            }
    }

    private fun saveRequestToken(token: String) {
        val editor = sharedPrefs.edit()
        editor.putString(REQUEST_TOKEN, token)
        editor.commit()
    }


    private fun saveSessionId(session: Session) {
        val editor = sharedPrefs.edit()
        editor.putString(SESSION, session.sessionId)
        editor.commit()
    }

    private fun saveTokenLifeTime(expiresAt: String) {
        val editor = sharedPrefs.edit()
        editor.putLong(REQUEST_LIFE, convertTime(expiresAt))
        editor.commit()
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
