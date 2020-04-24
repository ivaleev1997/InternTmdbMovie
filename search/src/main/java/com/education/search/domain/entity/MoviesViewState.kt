package com.education.search.domain.entity

import com.xwray.groupie.kotlinandroidextensions.Item

data class MoviesViewState(
    val moviesScreenState: MoviesScreenState = MoviesScreenState.ZERO,
    val listItems: List<Item> = listOf(),
    val isLinearLayoutRecyclerMap: Boolean = true,
    val loadFavoritesStatus: LoadFavoriteStatus = LoadFavoriteStatus.LOAD
)
