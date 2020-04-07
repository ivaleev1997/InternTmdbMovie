package com.education.core_api.data.network

import com.education.core_api.data.network.entity.*
import io.reactivex.Single
import retrofit2.http.*

interface TmdbMovieApi {

    @GET("search/movie")
    fun searchMovies(@Query("query") query: String): Single<MovieApiResponse<SearchMovie>>

    @GET("account")
    fun getAccountInfo(): Single<Account>

    @GET("account/{account_id}/favorite/movies")
    fun getFavoriteMovies(@Path("account_id") accountId: Long): Single<MovieApiResponse<SearchMovie>>

    @POST("account/{account_id}/favorite")
    fun markAsFavorite(
        @Path("account_id") accountId: Long,
        @Body requestFavoriteBody: RequestFavoriteBody
    ): Single<StatusResponse>

    @GET("movie/{movie_id}")
    fun getDetailsMovie(@Path("movie_id") movieId: Long): Single<DetailsMovie>

    @GET("genre/movie/list")
    fun getGenres(): Single<GenresResponse>
}
