package com.education.favorite.data

import com.education.core_api.data.network.TmdbMovieApi
import com.education.core_api.data.network.entity.DetailsMovie
import com.education.core_api.data.network.entity.MovieApiResponse
import com.education.core_api.data.network.entity.SearchMovie
import io.reactivex.Single
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
    private val tmdbMovieApi: TmdbMovieApi
) : FavoriteRepository {
    override fun loadFavorite(): Single<MovieApiResponse<SearchMovie>> {
        return tmdbMovieApi.getAccountInfo()
            .flatMap { account ->
                tmdbMovieApi.getFavoriteMovies(accountId = account.id)
        }
    }

    override fun loadDetails(long: Long): Single<DetailsMovie> {
        return tmdbMovieApi.getDetailsMovie(long)
    }
}