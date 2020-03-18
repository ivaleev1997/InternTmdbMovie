package com.education.core_impl.network.interceptor

import android.content.SharedPreferences
import com.education.core_api.PREFS_SESSION
import javax.inject.Inject
import okhttp3.Interceptor
import okhttp3.Response

class SessionIdInterceptor
    @Inject constructor (
        private val sharedPrefs: SharedPreferences
    ) : Interceptor {

    companion object {
        const val SESSION_PARAM = "session_id"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val originalHttpUrl = original.url()
        val url = originalHttpUrl
            .newBuilder()
            .addQueryParameter(
                SESSION_PARAM,
                getCurrentSessionId()
                )
            .build()

        val requestBuilder = original.newBuilder().url(url)
        val request = requestBuilder.build()

        return chain.proceed(request)
    }

    private fun getCurrentSessionId(): String {
        return sharedPrefs.getString(PREFS_SESSION, "") ?: ""
    }
}
