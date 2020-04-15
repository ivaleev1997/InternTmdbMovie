package com.education.movies.domain

import com.education.core_api.TMDB_IMAGE_URL
import com.education.core_api.data.network.entity.DetailsMovie
import com.education.core_api.data.network.entity.Genre
import com.education.core_api.data.network.entity.SearchMovie
import com.education.core_api.joinGenreArrayToString
import com.education.core_api.toOriginalTitleYear
import com.education.core_api.toTmdbPosterPath
import com.education.movies.data.repository.MoviesRepository
import com.education.search.domain.entity.Movie
import io.reactivex.Single
import javax.inject.Inject

class MoviesUseCase @Inject constructor(
    private val moviesRepository: MoviesRepository
) {
    fun searchQuery(query: String): Single<Pair<String, List<Movie>>> {
        return if (query.isBlank())
            Single.just(Pair(query, listOf()))
        else {
            moviesRepository.search(query)
                .flattenAsObservable { movieResponse -> movieResponse.movies }
                .flatMapSingle { searchMovie -> moviesRepository.loadDetails(searchMovie.id) }
                .toList()
                .map { listDetailsMovie -> detailsMapToPair(query, listDetailsMovie) }

/*            Flowable.combineLatest<MovieApiResponse<SearchMovie>, GenresResponse, Pair<String, List<Movie>>>(
                moviesRepository.search(query),
                moviesRepository.loadGenres(),
                BiFunction { moviesApiResponse: MovieApiResponse<SearchMovie>, genresResponse: GenresResponse ->
                    searchMapToPair(query, moviesApiResponse.movies, genresResponse.genres)
                })*/
        }
    }

    private fun detailsMapToPair(
        query: String,
        listDetailsMovie: List<DetailsMovie>
    ): Pair<String, List<Movie>> {
        return Pair(query,
            listDetailsMovie.map { detailsMovie ->
                Movie(
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

    private fun searchMapToPair(
        query: String,
        listSearchMovie: List<SearchMovie>,
        listGenres: List<Genre>
    ): Pair<String, List<Movie>> {
        return Pair(
            query,
            listSearchMovie.map { searchMovie ->
                Movie(
                    searchMovie.id,
                    TMDB_IMAGE_URL + searchMovie.posterPath,
                    searchMovie.title,
                    searchMovie.originalTitle,
                    searchMovie.genreIds.joinToString { genre ->
                        listGenres.first { it.id == genre }.genre
                    },
                    searchMovie.voteAverage,
                    searchMovie.voteCount,
                    ""
                )
            }
        )
    }
}
