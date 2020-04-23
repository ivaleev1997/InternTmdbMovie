package com.education.login.data.repository

import com.education.core_api.data.LocalDataSource
import com.education.core_api.data.network.TmdbAuthApi
import com.education.core_api.data.network.entity.RequestSessionBody
import com.education.core_api.data.network.entity.RequestToken
import com.education.core_api.data.network.entity.RequestTokenBody
import com.education.core_api.data.network.exception.SessionTokenException
import com.education.core_api.extension.flatMapCompletableAction
import com.education.login.domain.entity.User
import io.reactivex.Completable
import javax.inject.Inject

class LoginRepositoryImpl
@Inject constructor(
    private val tmdbAuthApi: TmdbAuthApi,
    private val localDataSource: LocalDataSource
) : LoginRepository {

    private var requestToken: String = ""
    private var sessionId: String = ""
    private var requestTokenLife: String = ""

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
            .flatMapCompletableAction { session ->
                if (session.success)
                    saveSessionId(session.sessionId)
                else
                    throw SessionTokenException()
            }
    }

    override fun getRequestToken() = requestToken

    override fun getSessionId() = sessionId

    override fun getRequestTokenLifeTime() = requestTokenLife

    private fun saveRequestToken(token: String) {
        // localDataSource.saveRequestToken(token)
        requestToken = token
    }

    private fun saveSessionId(session: String) {
        // localDataSource.saveSessionId(session)
        sessionId = session
    }

    private fun saveTokenLifeTime(expiresAt: String) {
        // localDataSource.saveTokenLifeTime(expiresAt)
        requestTokenLife = expiresAt
    }
}
