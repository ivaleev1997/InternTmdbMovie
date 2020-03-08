package com.education.core_impl

import com.education.core_api.network.NetworkContract
import com.education.core_api.network.TMDBApi
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideNetworkContract(): NetworkContract {
        return TMDBApiImpl()
    }

    @Provides
    @Singleton
    fun provideTMDBApi(networkContract: NetworkContract): TMDBApi {
        return networkContract.getInstance()
    }
}