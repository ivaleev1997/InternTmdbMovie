package com.education.movies.data.repository

import com.education.core_api.data.network.TmdbMovieApi
import com.education.core_api.data.network.entity.DetailsMovie
import com.education.core_api.data.network.entity.GenresResponse
import com.education.core_api.data.network.entity.MovieApiResponse
import com.education.core_api.data.network.entity.SearchMovie
import io.reactivex.Single
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    private val tmdbMovieApi: TmdbMovieApi
) : MoviesRepository {

    override fun search(query: String): Single<MovieApiResponse<SearchMovie>> {
        return tmdbMovieApi.searchMovies(query)
    }

    override fun loadGenres(): Single<GenresResponse> {
        return tmdbMovieApi.getGenres()
    }

    override fun loadDetails(long: Long): Single<DetailsMovie> {
        return tmdbMovieApi.getDetailsMovie(long)
    }
}
