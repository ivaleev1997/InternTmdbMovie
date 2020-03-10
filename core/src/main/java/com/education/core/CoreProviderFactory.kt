package com.education.core

import com.education.core_api.mediator.NetworkProvider
import com.education.core_impl.DaggerNetworkComponent

object CoreProviderFactory {

    fun createNetworkProvider(): NetworkProvider {
        return DaggerNetworkComponent.create()
    }
}
