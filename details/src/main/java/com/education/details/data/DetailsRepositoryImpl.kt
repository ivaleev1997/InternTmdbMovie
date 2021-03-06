package com.education.details.data

import com.education.core_api.data.LocalDataSource
import com.education.core_api.data.network.TmdbMovieApi
import com.education.core_api.data.network.entity.*
import io.reactivex.Single
import javax.inject.Inject

class DetailsRepositoryImpl @Inject constructor(
    private val tmdbMovieApi: TmdbMovieApi,
    private val localDataSource: LocalDataSource
) : DetailsRepository {
    override fun loadDetails(movieId: Long): Single<DetailsMovie> {
        return tmdbMovieApi.getDetailsMovie(movieId)
    }

    override fun changeFavorite(movieId: Long, flag: Boolean): Single<StatusResponse> {
        return tmdbMovieApi.getAccountInfo()
            .flatMap { account ->
                if (!flag)
                    localDataSource.deleteMovie(movieId)
                tmdbMovieApi.changeFavorite(account.id, RequestFavoriteBody(movieId, flag))
            }
    }

    override fun loadFavorites(): Single<MovieApiResponse<SearchMovie>> {
        return tmdbMovieApi.getAccountInfo()
            .flatMap { account ->
                tmdbMovieApi.getFavoriteMovies(account.id)
            }
    }
}
