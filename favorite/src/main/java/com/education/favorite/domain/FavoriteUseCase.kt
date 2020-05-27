package com.education.favorite.domain

import com.education.core_api.data.local.entity.Movie
import com.education.favorite.data.FavoriteRepository
import com.education.search.domain.entity.DomainMovie
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class FavoriteUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) {
    fun loadFavorites(): Single<List<DomainMovie>> {
        return favoriteRepository.loadFavorite()
            .map { movies -> repositoryMoviesToDomainMovies(movies) }
    }

    private fun repositoryMoviesToDomainMovies(movies: List<Movie>): List<DomainMovie> {
        return movies.map { movie -> DomainMovie(movie) }
    }

    fun getRecyclerMapState(): Single<Boolean> {
        return favoriteRepository.getRecyclerMapState()
    }

    fun saveRecyclerMapState(isLinearLayout: Boolean): Completable {
        return favoriteRepository.saveRecyclerMapState(isLinearLayout)
    }
}
