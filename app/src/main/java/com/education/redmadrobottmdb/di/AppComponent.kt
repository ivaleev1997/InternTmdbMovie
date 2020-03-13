package com.education.redmadrobottmdb.di

import com.education.core.CoreProviderFactory
import com.education.core_api.mediator.CoreProvider
import com.education.core_api.mediator.NetworkProvider
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [NetworkProvider::class],
    modules = [MediatorBindings::class]
)
interface AppComponent : CoreProvider {

    companion object {

        fun init(): AppComponent {
            return DaggerAppComponent
                .builder()
                .networkProvider(CoreProviderFactory.createNetworkProvider())
                .build()
        }
    }
}
