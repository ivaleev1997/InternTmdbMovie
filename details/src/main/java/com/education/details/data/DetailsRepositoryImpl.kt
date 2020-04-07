package com.education.details.data

import com.education.core_api.data.network.TmdbMovieApi
import com.education.core_api.data.network.entity.DetailsMovie
import com.education.core_api.data.network.entity.RequestFavoriteBody
import com.education.core_api.data.network.entity.StatusResponse
import io.reactivex.Single
import javax.inject.Inject

class DetailsRepositoryImpl @Inject constructor(
    private val tmdbMovieApi: TmdbMovieApi
) : DetailsRepository {
    override fun loadDetails(movieId: Long): Single<DetailsMovie> {
        return tmdbMovieApi.getDetailsMovie(movieId)
    }

    override fun markAsFavorite(movieId: Long, flag: Boolean): Single<StatusResponse> {
        return tmdbMovieApi.getAccountInfo()
            .flatMap { account ->
                tmdbMovieApi.markAsFavorite(account.id, RequestFavoriteBody(movieId, flag))
            }
    }
}