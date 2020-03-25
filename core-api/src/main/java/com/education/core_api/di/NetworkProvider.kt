package com.education.core_api.di

import com.education.core_api.data.network.TmdbAuthApi
import com.education.core_api.data.network.TmdbMovieApi

interface NetworkProvider {

    fun provideTmdbMovieApi(): TmdbMovieApi

    fun provideTmdbAuthApi(): TmdbAuthApi
}
