package com.education.movies.data.repository

import com.education.core_api.data.network.entity.DetailsMovie
import com.education.core_api.data.network.entity.GenresResponse
import com.education.core_api.data.network.entity.MovieApiResponse
import com.education.core_api.data.network.entity.SearchMovie
import io.reactivex.Single

interface MoviesRepository {

    fun search(query: String): Single<MovieApiResponse<SearchMovie>>

    fun loadGenres(): Single<GenresResponse>

    fun loadDetails(long: Long): Single<DetailsMovie>
}
