package com.education.core_api.dto

data class RequestTokenBody(
    val username: String,
    val password: String,
    val requestToken: String
)
