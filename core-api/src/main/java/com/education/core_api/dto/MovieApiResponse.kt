package com.education.core_api.dto

data class MovieApiResponse(
    val page: Int,
    val movies: List<Movie>,
    val totalResults: Int,
    val totalPages: Int
)
