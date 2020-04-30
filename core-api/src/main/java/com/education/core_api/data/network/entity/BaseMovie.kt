package com.education.core_api.data.network.entity

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