package com.education.core_api.data.network.entity

import com.google.gson.annotations.SerializedName

data class SearchMovie(
    @SerializedName("id")
    override val id: Long,
    @SerializedName("poster_path")
    override val posterPath: String,
    @SerializedName("vote_count")
    override val voteCount: Long,
    @SerializedName("original_title")
    override val originalTitle: String,
    @SerializedName("title")
    override val title: String,
    @SerializedName("vote_average")
    override val voteAverage: Double,
    @SerializedName("overview")
    override val overview: String,
    @SerializedName("release_date")
    override val releaseDate: String,
    @SerializedName("genre_ids")
    val genreIds: List<Int>
) : BaseMovie