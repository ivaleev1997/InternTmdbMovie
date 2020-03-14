package com.education.core_api.network

import com.education.core_api.dto.Movie
import com.education.core_api.dto.Session
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface TmdbAuthApi {
    @POST("/authentication/token/validate_with_login")
    fun validateRequestTokenWithLogin(): Single<Boolean>

    @POST("/authentication/session/new")
    fun createSessionId(): Single<Session>

    @GET("/search/movie")
    fun searchMovies(@Query("query") query: String): Single<Movie>
}