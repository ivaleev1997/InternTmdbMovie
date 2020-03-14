package com.education.core_impl.network

import com.education.core_api.network.TmdbAuthApi
import com.education.core_api.network.TmdbMovieApi
import com.education.core_impl.BuildConfig
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
class NetworkModule {

    companion object {
        const val TMDB_BASE_URL = "https://api.themoviedb.org/3/"
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()
    }

    @Provides
    @Singleton
    fun provideLogHttpInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideApiKeyInterceptor(): ApiKeyInterceptor {
        return ApiKeyInterceptor
    }

    @Provides
    @Singleton
    fun provideTmdbMovieOkHttpClient(
        apiKeyInterceptor: ApiKeyInterceptor,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder().apply {
            addInterceptor(apiKeyInterceptor)
            if (BuildConfig.DEBUG) addInterceptor(loggingInterceptor)
        }.build()
    }

    @Provides
    @Singleton
    fun providesTmdbAuthenticator(): TmdbAuthenticator {
        return TmdbAuthenticator
    }

    @Provides
    @Singleton
    @Named("okHttpAuth")
    fun provideTmdbAuthOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder().apply {
            if (BuildConfig.DEBUG) addInterceptor(loggingInterceptor)
        }.build()
    }

    @Provides
    @Singleton
    fun provideTmdbAuthApi(
        gson: Gson,
        @Named("okHttpAuth") okHttpClient: OkHttpClient
    ): TmdbAuthApi {
        return Retrofit.Builder()
            .baseUrl(TMDB_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(TmdbAuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideTmdbMovieApi(
        gson: Gson,
        okHttpClient: OkHttpClient
    ): TmdbMovieApi {
        return Retrofit.Builder()
            .baseUrl(TMDB_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(TmdbMovieApi::class.java)
    }
}
