package com.education.core_impl.data.network.interceptor

import com.education.core_api.BLANK_STR
import com.education.core_api.data.network.entity.StatusResponse
import com.education.core_api.data.network.exception.ServerException
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import okhttp3.Interceptor
import okhttp3.Response
import java.net.HttpURLConnection.HTTP_CREATED
import java.net.HttpURLConnection.HTTP_OK
import javax.inject.Inject

class NetworkErrorInterceptor
    @Inject constructor(
        private val gson: Gson
    ) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
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
                statusMessage?.statusMessage ?: BLANK_STR
            } catch (e: JsonSyntaxException) {
                BLANK_STR
            }
        } else BLANK_STR
    }
}
