package com.education.core_api.mediator

import com.education.core_api.network.TmdbAuthApi
import com.education.core_api.network.TmdbMovieApi

interface NetworkProvider {

    fun provideTmdbMovieApi(): TmdbMovieApi

    fun provideTmdbAuthApi(): TmdbAuthApi
}
