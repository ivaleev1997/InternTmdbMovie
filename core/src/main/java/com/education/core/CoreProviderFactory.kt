package com.education.core

import com.education.core_api.di.AppProvider
import com.education.core_api.di.LocalDataSourceProvider
import com.education.core_api.di.NetworkProvider
import com.education.core_api.presentation.viewmodel.ViewModelsProvider
import com.education.core_impl.di.component.DaggerLocalDataSourceComponent
import com.education.core_impl.di.component.DaggerNetworkComponent
import com.education.core_impl.di.component.DaggerViewModelComponent

object CoreProviderFactory {

    private var viewModelsProvider: ViewModelsProvider? = null
    private var networkProvider: NetworkProvider? = null
    private var localDataSourceProvider: LocalDataSourceProvider? = null

    fun createNetworkProvider(localDataSourceProvider: LocalDataSourceProvider): NetworkProvider {
        return networkProvider ?: DaggerNetworkComponent
            .builder()
            .localDataSourceProvider(localDataSourceProvider)
            .build().also {
                networkProvider = it
            }
    }

    fun createViewModelProvider(): ViewModelsProvider {
        return viewModelsProvider ?: DaggerViewModelComponent.create().also { viewModelsProvider = it }
    }

    fun createLocalDataSourceProvider(appProvider: AppProvider): LocalDataSourceProvider {
        return localDataSourceProvider ?: DaggerLocalDataSourceComponent
            .builder()
            .appProvider(appProvider)
            .build().also {
                localDataSourceProvider = it
            }
    }
}
