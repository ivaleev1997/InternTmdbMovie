package com.education.favorite.domain

import com.education.favorite.data.FavoriteRepository
import com.education.search.domain.entity.Movie
import com.education.search.domain.MoviesRecyclerUseCase
import io.reactivex.Single
import javax.inject.Inject

class FavoriteUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) : MoviesRecyclerUseCase() {
    fun loadFavorites(): Single<List<Movie>> {
        return favoriteRepository.loadFavorite()
            .flattenAsObservable { moviesApiResponse -> moviesApiResponse.movies }
            .flatMapSingle { searchMovie -> favoriteRepository.loadDetails(searchMovie.id) }
            .toList()
            .map { detailsListMovie -> detailsMovieMapToMovie(detailsListMovie) }
    }
}