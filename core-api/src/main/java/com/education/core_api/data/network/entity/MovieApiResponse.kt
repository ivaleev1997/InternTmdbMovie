package com.education.core_api.data.network.entity

import com.google.gson.annotations.SerializedName

data class MovieApiResponse<T>(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val movies: List<T>,
    @SerializedName("total_results")
    val totalResults: Int,
    @SerializedName("total_pages")
    val totalPages: Int
)
