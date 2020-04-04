package com.education.core_api.data.network.entity

import com.google.gson.annotations.SerializedName

interface BaseMovie {
    val id: Long
    val posterPath: String?
    val voteCount: Long
    val originalTitle: String
    val title: String
    val voteAverage: Double
    val overview: String
    val releaseDate: String
}

data class DetailsMovie(
    @SerializedName("id")
    override val id: Long,
    @SerializedName("original_title")
    override val originalTitle: String,
    @SerializedName("poster_path")
    override val posterPath: String?,
    @SerializedName("overview")
    override val overview: String,
    @SerializedName("release_date")
    override val releaseDate: String,
    @SerializedName("vote_count")
    override val voteCount: Long,
    @SerializedName("vote_average")
    override val voteAverage: Double,
    @SerializedName("title")
    override val title: String,
    @SerializedName("popularity")
    val popularity: Double,
    @SerializedName("runtime")
    val runTime: Int,
    @SerializedName("genres")
    val genres: List<Genre>
) : BaseMovie

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
