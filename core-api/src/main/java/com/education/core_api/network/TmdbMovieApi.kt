package com.education.core_api.network

import com.education.core_api.dto.RequestToken
import io.reactivex.Single
import retrofit2.http.GET

interface TmdbMovieApi {
    @GET("/authentication/token/new")
    fun createRequestToken(): Single<RequestToken>
}
