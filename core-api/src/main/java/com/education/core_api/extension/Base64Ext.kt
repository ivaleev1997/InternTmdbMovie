package com.education.core_api.extension

import android.util.Base64

fun ByteArray.base64EncodeToString(): String = Base64.encodeToString(this, Base64.NO_WRAP)

fun String.base64DecodeFromString(): ByteArray = Base64.decode(this, Base64.NO_WRAP)