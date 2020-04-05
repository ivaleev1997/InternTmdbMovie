package com.education.movies.domain.entity

import com.education.search.domain.entity.Movie

data class MoviesViewState(
    val movies: List<Movie>,
    val moviesListState: MoviesListState
)
