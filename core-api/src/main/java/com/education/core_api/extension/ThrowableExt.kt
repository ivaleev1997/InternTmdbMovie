package com.education.core_api.extension

import java.net.*

fun Throwable?.isNetworkException() = when (this) {
    is ConnectException,
    is SocketException,
    is SocketTimeoutException,
    is UnknownHostException,
    is ProtocolException -> true
    else -> false
}
