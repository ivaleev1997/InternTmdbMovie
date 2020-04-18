package com.education.favorite.domain

import com.education.core_api.data.local.entuty.Movie
import com.education.core_api.toOriginalTitleYear
import com.education.core_api.toTmdbPosterPath
import com.education.favorite.data.FavoriteRepository
import com.education.search.domain.entity.DomainMovie
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
        return movies.map { movie ->
            DomainMovie(
                movie.id,
                movie.posterPath.toTmdbPosterPath(),
                movie.title,
                movie.originalTitle + movie.releaseDate.toOriginalTitleYear(),
                movie.genres,
                movie.voteAverage,
                movie.voteCount,
                movie.runTime.toString()
            )
        }
    }
}