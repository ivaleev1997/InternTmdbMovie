package com.education.core_impl.di.module

import com.education.core_api.data.network.TmdbAuthApi
import com.education.core_api.data.network.TmdbMovieApi
import com.education.core_impl.BuildConfig
import com.education.core_impl.data.network.TmdbAuthenticator
import com.education.core_impl.data.network.interceptor.ApiKeyInterceptor
import com.education.core_impl.data.network.interceptor.LanguageInterceptor
import com.education.core_impl.data.network.interceptor.NetworkErrorInterceptor
import com.education.core_impl.data.network.interceptor.SessionIdInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
class NetworkModule {
    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .create()
    }

    @Provides
    @Singleton
    fun provideLogHttpInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
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
    fun provideCertificatePinner(): CertificatePinner {
        return CertificatePinner.Builder()
            .add(BuildConfig.SSL_SERVER_PATTERN,
                BuildConfig.SSL_PIN)
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClientBuilder(
        authenticator: TmdbAuthenticator,
        apiKeyInterceptor: ApiKeyInterceptor,
        loggingInterceptor: HttpLoggingInterceptor,
        networkErrorInterceptor: NetworkErrorInterceptor,
        certificatePinner: CertificatePinner
    ): OkHttpClient.Builder {
        return OkHttpClient.Builder().apply {
            authenticator(authenticator)
            addInterceptor(apiKeyInterceptor)
            addInterceptor(networkErrorInterceptor)
            //certificatePinner(certificatePinner)
            retryOnConnectionFailure(true)
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
            .baseUrl(BuildConfig.SERVER_ADDR)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }
}
