package com.education.favorite.data

import com.education.core_api.data.local.entuty.Movie
import io.reactivex.Single

interface FavoriteRepository {
    fun loadFavorite(): Single<List<Movie>>
}