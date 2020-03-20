package com.education.core_impl.data.network.interceptor

import android.annotation.SuppressLint
import android.net.ConnectivityManager
import com.education.core_api.data.network.entity.StatusResponse
import com.education.core_api.data.network.exception.NoInternetConnectionException
import com.education.core_api.data.network.exception.ServerException
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import java.net.HttpURLConnection.HTTP_CREATED
import java.net.HttpURLConnection.HTTP_OK
import javax.inject.Inject
import okhttp3.Interceptor
import okhttp3.Response

class NetworkErrorInterceptor
    @Inject constructor(
        private val connectivityManager: ConnectivityManager,
        private val gson: Gson
    ) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!hasNetworkConnection())
            throw NoInternetConnectionException()

        val request = chain.request()
        val response = chain.proceed(request)

        return when (response.code()) {
            HTTP_OK -> response
            HTTP_CREATED -> response
            else -> throw ServerException(getStatusMessage(response))
        }
    }

    private fun getStatusMessage(response: Response): String {
        val body = response.body()

        return if (body != null) {
            try {
                val statusMessage = gson.fromJson(body.string(), StatusResponse::class.java)
                statusMessage?.statusMessage ?: ""
            } catch (e: JsonSyntaxException) {
                ""
            }
        } else ""
    }

    @SuppressLint("MissingPermission")
    private fun hasNetworkConnection(): Boolean {
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}
