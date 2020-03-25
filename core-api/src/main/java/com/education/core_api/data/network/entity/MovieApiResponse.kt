package com.education.core_api.data.network.entity

import com.google.gson.annotations.SerializedName

data class MovieApiResponse(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val movies: List<Movie>,
    @SerializedName("total_results")
    val totalResults: Int,
    @SerializedName("total_pages")
    val totalPages: Int
)
