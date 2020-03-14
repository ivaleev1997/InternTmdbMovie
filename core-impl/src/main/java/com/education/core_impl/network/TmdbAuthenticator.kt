package com.education.core_impl.network

import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

object TmdbAuthenticator : Authenticator {
    @Synchronized
    override fun authenticate(route: Route?, response: Response): Request? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}