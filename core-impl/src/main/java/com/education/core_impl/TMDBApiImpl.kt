package com.education.core_impl

import com.education.core_api.network.NetworkContract
import com.education.core_api.network.TMDBApi
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TMDBApiImpl : NetworkContract {

    companion object {
        const val TMDB_BASE_URL = "https://api.themoviedb.org/3/"
        const val TMDB_API_KEY = "aa7b2f0df06cb6ccf1cbcf705bcf9892"
    }

    override fun getInstance(): TMDBApi {
        return tmdbApi
    }

    private val tmdbApi: TMDBApi by lazy {
        val okHttpClient = OkHttpClient.Builder().apply {
            addInterceptor { chain ->
                val original = chain.request()
                val originalHttpUrl = original.url()
                val url = originalHttpUrl
                    .newBuilder()
                    .addQueryParameter("api_key", TMDB_API_KEY)
                    .build()

                val requestBuilder = original.newBuilder().url(url)
                val request = requestBuilder.build()
                chain.proceed(request)
            }
            if (BuildConfig.DEBUG)
                addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
        }.build()

        val gson = GsonBuilder().create()

        return@lazy Retrofit.Builder()
            .baseUrl(TMDB_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(TMDBApi::class.java)
    }
}
