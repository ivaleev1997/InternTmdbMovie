package com.education.core_impl.network

import com.education.core_api.TMDB_BASE_URL
import com.education.core_api.network.TmdbAuthApi
import com.education.core_api.network.TmdbMovieApi
import com.education.core_impl.BuildConfig
import com.education.core_impl.network.interceptor.ApiKeyInterceptor
import com.education.core_impl.network.interceptor.LanguageInterceptor
import com.education.core_impl.network.interceptor.NetworkErrorInterceptor
import com.education.core_impl.network.interceptor.SessionIdInterceptor
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
class NetworkModule {
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
    fun provideApiKeyInterceptor(): ApiKeyInterceptor {
        return ApiKeyInterceptor
    }

    @Provides
    fun provideLanguageInterceptor(): LanguageInterceptor {
        return LanguageInterceptor
    }

    @Provides
    @Singleton
    fun provideTmdbAuthenticator(): TmdbAuthenticator {
        return TmdbAuthenticator()
    }

    @Provides
    @Singleton
    fun provideOkHttpClientBuilder(
        authenticator: TmdbAuthenticator,
        apiKeyInterceptor: ApiKeyInterceptor,
        loggingInterceptor: HttpLoggingInterceptor,
        networkErrorInterceptor: NetworkErrorInterceptor
    ): OkHttpClient.Builder {
        return OkHttpClient.Builder().apply {
            authenticator(authenticator)
            addInterceptor(apiKeyInterceptor)
            addInterceptor(networkErrorInterceptor)
            if (BuildConfig.DEBUG) addInterceptor(loggingInterceptor)
        }
    }

    @Provides
    @Singleton
    fun provideTmdbMovieOkHttpClient(
        okHttpClientBuilder: OkHttpClient.Builder,
        languageInterceptor: LanguageInterceptor,
        sessionIdInterceptor: SessionIdInterceptor
    ): OkHttpClient {
        return okHttpClientBuilder.apply {
            addInterceptor(languageInterceptor)
            addInterceptor(sessionIdInterceptor)
        }.build()
    }

    @Provides
    @Singleton
    @Named("okHttpAuth")
    fun provideTmdbAuthOkHttpClient(
        okHttpClientBuilder: OkHttpClient.Builder
    ): OkHttpClient {
        return okHttpClientBuilder.build()
    }

    @Provides
    @Singleton
    fun provideTmdbMovieApi(
        gson: Gson,
        okHttpClient: OkHttpClient
    ): TmdbMovieApi {
        return createTmdbRetrofit(gson, okHttpClient).create(TmdbMovieApi::class.java)
    }

    @Provides
    @Singleton
    fun provideTmdbAuthApi(
        gson: Gson,
        @Named("okHttpAuth") okHttpClient: OkHttpClient
    ): TmdbAuthApi {
        return createTmdbRetrofit(gson, okHttpClient).create(TmdbAuthApi::class.java)
    }

    private fun createTmdbRetrofit(
        gson: Gson,
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(TMDB_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }
}
