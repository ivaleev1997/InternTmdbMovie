package com.education.favorite.data

import com.education.core_api.data.local.entity.Movie
import io.reactivex.Completable
import io.reactivex.Single

interface FavoriteRepository {
    fun loadFavorite(): Single<List<Movie>>

    fun saveRecyclerMapState(flag: Boolean): Completable

    fun getRecyclerMapState(): Single<Boolean>
}
