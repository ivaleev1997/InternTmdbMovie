package com.education.movies.domain.entity

import com.xwray.groupie.kotlinandroidextensions.Item

data class MoviesViewState(
    val moviesScreenState: MoviesScreenState,
    val listItems: List<Item>,
    val isLineRecyclerMap: Boolean
)
