package com.education.movies.domain.entity

import com.education.search.entity.Movie

data class MoviesViewState(
    val movies: List<Movie>,
    val moviesListState: MoviesListState
)
