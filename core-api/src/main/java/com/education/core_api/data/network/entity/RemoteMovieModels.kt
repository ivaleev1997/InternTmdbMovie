package com.education.core_api.data.network.entity

import com.education.core_api.MOVIE_IS_WORTH_WATCHING_CONDITION
import com.education.core_api.data.local.entity.Movie
import com.education.core_api.joinGenreArrayToString
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
) : BaseMovie {

    fun toMovie(): Movie {
        return Movie(
            this.id,
            this.originalTitle,
            this.posterPath,
            this.overview,
            this.releaseDate,
            this.voteCount,
            this.voteAverage,
            this.title,
            this.popularity,
            this.runTime,
            this.genres.joinGenreArrayToString(),
            this.voteAverage > MOVIE_IS_WORTH_WATCHING_CONDITION
        )
    }
}

