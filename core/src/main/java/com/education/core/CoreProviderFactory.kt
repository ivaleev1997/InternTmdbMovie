package com.education.core

import com.education.core_api.di.AppProvider
import com.education.core_api.di.LocalDataSourceProvider
import com.education.core_api.di.NetworkProvider
import com.education.core_api.presentation.viewmodel.ViewModelsProvider
import com.education.core_impl.di.component.DaggerLocalDataSourceComponent
import com.education.core_impl.di.component.DaggerNetworkComponent
import com.education.core_impl.di.component.DaggerViewModelComponent

object CoreProviderFactory {

    fun createNetworkProvider(localDataSourceProvider: LocalDataSourceProvider): NetworkProvider {
        return DaggerNetworkComponent
            .builder()
            .localDataSourceProvider(localDataSourceProvider)
            .build()
    }

    fun createViewModelProvider(): ViewModelsProvider {
        return DaggerViewModelComponent.create()
    }

    fun createLocalDataSourceProvider(appProvider: AppProvider): LocalDataSourceProvider {
        return DaggerLocalDataSourceComponent
            .builder()
            .appProvider(appProvider)
            .build()
    }
}
