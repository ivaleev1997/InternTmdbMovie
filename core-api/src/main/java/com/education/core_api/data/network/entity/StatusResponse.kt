package com.education.core_api.data.network.entity

import com.google.gson.annotations.SerializedName

data class StatusResponse(
    @SerializedName("status_code")
    val statusCode: Long,
    @SerializedName("status_message")
    val statusMessage: String
)
