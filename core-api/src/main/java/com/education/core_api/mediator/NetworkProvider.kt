package com.education.core_api.mediator

import com.education.core_api.network.NetworkContract
import com.education.core_api.network.TMDBApi

interface NetworkProvider {

    fun provideNetwork(): NetworkContract

    fun provideTMDBApi(): TMDBApi
}
