package com.education.search.domain.entity

import com.education.core_api.data.local.entity.Movie
import com.education.core_api.toOriginalTitleYear
import com.education.core_api.toTmdbPosterPath

data class DomainMovie(
    val id: Long,
    val posterPath: String,
    val title: String,
    val originalTitle: String,
    val genre: String,
    val voteAverage: Double,
    val voteCount: Long,
    val runTime: String
) {
    constructor(movie: Movie) : this(
        movie.id,
        movie.posterPath.toTmdbPosterPath(),
        movie.title,
        movie.originalTitle + movie.releaseDate.toOriginalTitleYear(),
        movie.genres,
        movie.voteAverage,
        movie.voteCount,
        movie.runTime.toString()
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DomainMovie

        if (id != other.id) return false
        if (title != other.title) return false
        if (originalTitle != other.originalTitle) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + originalTitle.hashCode()
        return result
    }
}
