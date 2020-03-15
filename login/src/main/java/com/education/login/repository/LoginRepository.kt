package com.education.login.repository

import android.content.SharedPreferences
import com.education.core_api.SESSION
import com.education.core_api.dto.*
import com.education.core_api.network.TmdbAuthApi
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class LoginRepository
@Inject constructor(
    private val tmdbAuthApi: TmdbAuthApi,
    private val sharedPrefs: SharedPreferences
) {

    fun login(user: User): Single<Boolean> {
        return tmdbAuthApi.createRequestToken()
            .subscribeOn(Schedulers.io())
            .flatMap { requestToken: RequestToken ->
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
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun saveSessionId(session: Session) {
        val editor = sharedPrefs.edit()
        editor.putString(SESSION, session.sessionId)
        editor.commit()
    }
}
