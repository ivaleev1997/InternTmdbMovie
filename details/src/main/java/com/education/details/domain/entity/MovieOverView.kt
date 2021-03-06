package com.education.details.domain.entity

data class MovieOverView(
    val id: Long,
    val title: String,
    val originalTitle: String,
    val genre: String,
    val posterPath: String,
    val voteAverage: String,
    val voteCount: String,
    val runTime: String,
    val overView: String,
    val isFavorite: Boolean = false
)
