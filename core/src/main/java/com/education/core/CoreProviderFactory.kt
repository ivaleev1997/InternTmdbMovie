package com.education.core

import com.education.core_api.di.AppProvider
import com.education.core_api.di.NetworkProvider
import com.education.core_api.presentation.viewmodel.ViewModelsProvider
import com.education.core_impl.di.component.DaggerNetworkComponent
import com.education.core_impl.di.component.DaggerViewModelComponent

object CoreProviderFactory {

    fun createNetworkProvider(appProvider: AppProvider): NetworkProvider {
        return DaggerNetworkComponent
            .builder()
            .appProvider(appProvider)
            .build()
    }

    fun createViewModelProvider(): ViewModelsProvider {
        return DaggerViewModelComponent.create()
    }
}
