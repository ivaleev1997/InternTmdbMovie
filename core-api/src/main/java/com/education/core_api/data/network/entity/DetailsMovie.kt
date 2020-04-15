package com.education.core_api.data.network.entity

import com.google.gson.annotations.SerializedName

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

