package com.education.movies.data.repository

import com.education.core_api.data.network.entity.GenresResponse
import com.education.core_api.data.network.entity.MovieApiResponse
import com.education.core_api.data.network.entity.SearchMovie
import io.reactivex.Flowable

interface MoviesRepository {

    fun search(query: String): Flowable<MovieApiResponse<SearchMovie>>

    fun loadGenres(): Flowable<GenresResponse>
}