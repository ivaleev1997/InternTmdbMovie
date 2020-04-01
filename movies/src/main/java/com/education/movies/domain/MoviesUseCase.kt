package com.education.movies.domain

import com.education.core_api.TMDB_IMAGE_URL
import com.education.core_api.data.network.entity.GenresResponse
import com.education.core_api.data.network.entity.MovieApiResponse
import com.education.core_api.data.network.entity.SearchMovie
import com.education.movies.data.repository.MoviesRepository
import com.education.search.entity.Movie
import io.reactivex.Flowable
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class MoviesUseCase @Inject constructor(
    private val moviesRepository: MoviesRepository
) {
    fun searchQuery(query: String): Flowable<Pair<String, List<Movie>>> {
        return if (query.isBlank())
            Flowable.just(Pair(query, listOf()))
        else {
            Flowable.combineLatest<MovieApiResponse<SearchMovie>, GenresResponse, Pair<String, List<Movie>>>(
                moviesRepository.search(query),
                moviesRepository.loadGenres(),
                BiFunction { moviesApiResponse: MovieApiResponse<SearchMovie>, genresResponse: GenresResponse ->
                    Pair(
                        query,
                        moviesApiResponse.movies.map { searchMovie ->
                            Movie(
                                searchMovie.id,
                                TMDB_IMAGE_URL + searchMovie.posterPath,
                                searchMovie.title,
                                searchMovie.originalTitle,
                                searchMovie.genreIds.joinToString { genre ->
                                    genresResponse.genres.first { it.id == genre }.genre
                                },
                                searchMovie.voteAverage,
                                searchMovie.voteCount,
                                ""
                            )
                        }
                    )
                })
        }
    }
}