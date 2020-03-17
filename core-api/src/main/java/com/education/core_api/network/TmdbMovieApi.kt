package com.education.core_api.network

import com.education.core_api.dto.*
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbMovieApi {

    @GET("search/movie")
    fun searchMovies(@Query("query") query: String): Single<Movie>

    @GET("account")
    fun getAccountInfo(): Single<Account>

    @GET("account/{account_id}/favorite/movies")
    fun getFavoriteMovies(@Path("account_id") accountId: Long): Single<MovieApiResponse>

    @POST("account/{account_id}/favorite")
    fun markAsFavorite(
        @Path("account_id") accountId: Long,
        requestFavoriteBody: RequestFavoriteBody
    ): Single<StatusResponse>
}
