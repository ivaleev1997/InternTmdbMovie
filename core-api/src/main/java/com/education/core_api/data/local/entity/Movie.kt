package com.education.core_api.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Movie(
    @PrimaryKey
    val id: Long,
    val originalTitle: String,
    val posterPath: String?,
    val overview: String,
    val releaseDate: String,
    val voteCount: Long,
    val voteAverage: Double,
    val title: String,
    val popularity: Double,
    val runTime: Int,
    val genres: String,
    val isWorthWatching: Boolean
)
