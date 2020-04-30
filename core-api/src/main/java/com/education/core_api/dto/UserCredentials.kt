package com.education.core_api.dto

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserCredentials(
    val userLogin: String,
    val userPassword: String,
    val requestToken: String,
    val requestTokenLife: String,
    val sessionId: String
) : Parcelable
