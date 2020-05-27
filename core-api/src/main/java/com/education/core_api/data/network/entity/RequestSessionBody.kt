package com.education.core_api.data.network.entity

import com.google.gson.annotations.SerializedName

data class RequestSessionBody(
    @SerializedName("request_token")
    val requestToken: String
)
