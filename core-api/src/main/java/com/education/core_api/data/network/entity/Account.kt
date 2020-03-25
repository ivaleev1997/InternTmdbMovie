package com.education.core_api.data.network.entity

import com.google.gson.annotations.SerializedName

data class Account(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("username")
    val username: String
)
