package com.education.core_api.data.network

import com.education.core_api.data.network.entity.RequestSessionBody
import com.education.core_api.data.network.entity.RequestToken
import com.education.core_api.data.network.entity.RequestTokenBody
import com.education.core_api.data.network.entity.Session
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface TmdbAuthApi {
    @POST("authentication/token/validate_with_login")
    fun validateRequestTokenWithLogin(@Body requestTokenBody: RequestTokenBody): Single<RequestToken>

    @POST("authentication/session/new")
    fun createSessionId(@Body requestToken: RequestSessionBody): Single<Session>

    @GET("authentication/token/new")
    fun createRequestToken(): Single<RequestToken>
}
