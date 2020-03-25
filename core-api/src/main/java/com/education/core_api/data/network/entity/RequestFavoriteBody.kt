package com.education.core_api.data.network.entity

import com.google.gson.annotations.SerializedName

data class RequestFavoriteBody(
    @SerializedName("media_id")
    val mediaId: Long,
    @SerializedName("favorite")
    val favorite: Boolean,
    @SerializedName("media_type")
    val mediaType: String = "movie"
)
