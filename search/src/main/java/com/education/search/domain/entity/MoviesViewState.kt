package com.education.search.domain.entity

import com.xwray.groupie.kotlinandroidextensions.Item

data class MoviesViewState(
    val moviesScreenState: MoviesScreenState,
    val listItems: List<Item>,
    val isLinearLayoutRecyclerMap: Boolean = true
)
