package com.education.movies.data.repository

import com.education.core_api.data.network.TmdbMovieApi
import com.education.core_api.data.network.entity.GenresResponse
import com.education.core_api.data.network.entity.MovieApiResponse
import com.education.core_api.data.network.entity.SearchMovie
import io.reactivex.Flowable
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    private val tmdbMovieApi: TmdbMovieApi
): MoviesRepository {

    override fun search(query: String): Flowable<MovieApiResponse<SearchMovie>> {
        return tmdbMovieApi.searchMovies(query)
    }

    override fun loadGenres(): Flowable<GenresResponse> {
        return tmdbMovieApi.getGenres()
    }
}