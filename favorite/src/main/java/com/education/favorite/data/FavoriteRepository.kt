package com.education.favorite.data

import com.education.core_api.data.network.entity.DetailsMovie
import com.education.core_api.data.network.entity.MovieApiResponse
import com.education.core_api.data.network.entity.SearchMovie
import io.reactivex.Single

interface FavoriteRepository {
    fun loadFavorite(): Single<MovieApiResponse<SearchMovie>>

    fun loadDetails(long: Long): Single<DetailsMovie>
}