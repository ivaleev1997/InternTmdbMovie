package com.education.search.domain.entity

data class Movie(
    val id: Long,
    val posterPath: String,
    val title: String,
    val originalTitle: String,
    val genre: String,
    val voteAverage: Double,
    val voteCount: Long,
    val runTime: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Movie

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
