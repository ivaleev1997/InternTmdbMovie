package com.education.redmadrobottmdb.di

import com.education.core.CoreProviderFactory
import com.education.core_api.mediator.NetworkProvider
import com.education.core_api.mediator.ProvidersFacade
import dagger.Component

@Component(
    dependencies = [NetworkProvider::class],
    modules = [MediatorBindings::class]
)
interface FacadeComponent : ProvidersFacade {

    companion object {

        fun init(): FacadeComponent {
            return DaggerFacadeComponent
                .builder()
                .networkProvider(CoreProviderFactory.createNetworkProvider())
                .build()
        }
    }
}