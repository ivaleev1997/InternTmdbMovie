package com.education.core_impl.data.network

import com.education.core_api.data.network.exception.UnAuthorizedException
import java.net.HttpURLConnection.HTTP_UNAUTHORIZED
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class TmdbAuthenticator : Authenticator {
    @Synchronized
    override fun authenticate(route: Route?, response: Response?): Request? {
        if (response == null) return null
        if (response.code() == HTTP_UNAUTHORIZED) {
            throw UnAuthorizedException(
                "Unauthorized response code ${response.code()}"
            )
        }

        return null
    }
}
