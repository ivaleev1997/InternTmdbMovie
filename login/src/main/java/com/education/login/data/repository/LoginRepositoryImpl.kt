package com.education.login.data.repository

import android.content.SharedPreferences
import com.education.core_api.PREFS_REQUEST_LIFE
import com.education.core_api.PREFS_REQUEST_TOKEN
import com.education.core_api.PREFS_SESSION
import com.education.core_api.convertTime
import com.education.core_api.data.network.TmdbAuthApi
import com.education.core_api.data.network.entity.RequestSessionBody
import com.education.core_api.data.network.entity.RequestToken
import com.education.core_api.data.network.entity.RequestTokenBody
import com.education.core_api.data.network.exception.SessionTokenException
import com.education.core_api.extension.putLong
import com.education.core_api.extension.putString
import com.education.login.domain.entity.User
import io.reactivex.Completable
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
                        user.password,
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
            .ignoreElement()
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
}
