package com.education.core_api.mediator

import com.education.core_api.network.TmdbMovieApi

interface NetworkProvider {

    fun provideTMDBApi(): TmdbMovieApi
}
