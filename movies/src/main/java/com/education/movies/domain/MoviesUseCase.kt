package com.education.movies.domain

import com.education.core_api.data.network.entity.DetailsMovie
import com.education.core_api.joinGenreArrayToString
import com.education.core_api.toOriginalTitleYear
import com.education.core_api.toTmdbPosterPath
import com.education.movies.data.repository.MoviesRepository
import com.education.search.domain.entity.DomainMovie
import io.reactivex.Single
import javax.inject.Inject

class MoviesUseCase @Inject constructor(
    private val moviesRepository: MoviesRepository
) {
    fun searchQuery(query: String): Single<Pair<String, List<DomainMovie>>> {
        return if (query.isBlank())
            Single.just(Pair(query, listOf()))
        else {
            moviesRepository.search(query)
                .flattenAsObservable { movieResponse -> movieResponse.movies }
                .flatMapSingle { searchMovie -> moviesRepository.loadDetails(searchMovie.id) }
                .toList()
                .map { listDetailsMovie -> detailsMapToPair(query, listDetailsMovie) }
        }
    }

    private fun detailsMapToPair(
        query: String,
        listDetailsMovie: List<DetailsMovie>
    ): Pair<String, List<DomainMovie>> {
        return Pair(query,
            listDetailsMovie.map { detailsMovie ->
                DomainMovie(
                    detailsMovie.id,
                    detailsMovie.posterPath.toTmdbPosterPath(),
                    detailsMovie.title,
                    detailsMovie.originalTitle + detailsMovie.releaseDate.toOriginalTitleYear(),
                    detailsMovie.genres.joinGenreArrayToString(),
                    detailsMovie.voteAverage,
                    detailsMovie.voteCount,
                    detailsMovie.runTime.toString()
                )
            }
        )
    }
}
