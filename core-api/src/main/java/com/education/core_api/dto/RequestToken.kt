package com.education.core_api.dto

data class RequestToken(
    val success: Boolean,
    val expiresAt: String,
    val requestToken: String
)
