package com.education.core_api.dto

data class RequestFavoriteBody(
    val mediaId: Long,
    val favorite: Boolean,
    val mediaType: String = "movie"
)