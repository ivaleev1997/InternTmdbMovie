package com.education.core_impl.network

import com.education.core_api.network.UnAuthorizedException
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import java.net.HttpURLConnection.HTTP_UNAUTHORIZED

class TmdbAuthenticator : Authenticator {
    @Synchronized
    override fun authenticate(route: Route?, response: Response?): Request? {
        if (response == null) return null
        if (response.code() == HTTP_UNAUTHORIZED) {
            throw UnAuthorizedException("Unauthorized response code ${response.code()}")
        }

        return null
    }
}