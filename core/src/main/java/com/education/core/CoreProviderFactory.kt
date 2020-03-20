package com.education.core

import com.education.core_api.mediator.NetworkProvider
import com.education.core_api.viewmodel.ViewModelsProvider
import com.education.core_impl.network.DaggerNetworkComponent
import com.education.core_impl.viewmodel.DaggerViewModelComponent

object CoreProviderFactory {

    fun createNetworkProvider(): NetworkProvider {
        return DaggerNetworkComponent.create()
    }

    fun createViewModelProvider(): ViewModelsProvider {
        return DaggerViewModelComponent.create()
    }
}
