package com.education.details.data

import com.education.core_api.data.network.entity.DetailsMovie
import com.education.core_api.data.network.entity.MovieApiResponse
import com.education.core_api.data.network.entity.SearchMovie
import com.education.core_api.data.network.entity.StatusResponse
import io.reactivex.Single

interface DetailsRepository {
    fun loadDetails(movieId: Long): Single<DetailsMovie>

    fun markAsFavorite(movieId: Long, flag: Boolean): Single<StatusResponse>

    fun loadFavorites(): Single<MovieApiResponse<SearchMovie>>
}