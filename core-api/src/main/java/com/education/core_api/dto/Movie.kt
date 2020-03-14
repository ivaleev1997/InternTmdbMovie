package com.education.core_api.dto

data class Movie(
    val id: Long,
    val originalTitle: String,
    val posterPath: String,
    val description: String,
    val releaseDate: String,
    val adult: Boolean,
    val backdropPath: String,
    val popularity: Double,
    val voteCount: Int,
    val voteAverage: Double
)
