package com.education.core

import com.education.core_api.mediator.AppProvider
import com.education.core_api.mediator.NetworkProvider
import com.education.core_api.viewmodel.ViewModelsProvider
import com.education.core_impl.network.DaggerNetworkComponent
import com.education.core_impl.viewmodel.DaggerViewModelComponent

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
