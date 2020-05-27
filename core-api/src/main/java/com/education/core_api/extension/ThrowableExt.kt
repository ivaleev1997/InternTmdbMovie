package com.education.core_api.extension

import com.education.core_api.data.network.exception.ServerException
import java.net.*

fun Throwable?.isNetworkException() = when (this) {
    is ConnectException,
    is SocketException,
    is SocketTimeoutException,
    is UnknownHostException,
    is ProtocolException,
    is ServerException -> true
    else -> false
}
