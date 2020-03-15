package com.education.redmadrobottmdb.di.component

import android.app.Application
import com.education.core.CoreProviderFactory
import com.education.core_api.mediator.AppProvider
import com.education.core_api.mediator.CoreProvider
import com.education.core_api.mediator.NetworkProvider
import com.education.redmadrobottmdb.di.module.MediatorBindings
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [NetworkProvider::class, AppProvider::class],
    modules = [MediatorBindings::class]
)
interface CoreComponent : CoreProvider {

    companion object {

        private var coreComponent: CoreComponent? = null

        fun init(application: Application): CoreComponent {
            val appProvider = AppComponent.create(application)
            return coreComponent
                ?: DaggerCoreComponent.builder()
                .appProvider(appProvider)
                .networkProvider(CoreProviderFactory.createNetworkProvider(appProvider))
                .build()
                .also {
                    coreComponent = it
                }
        }
    }
}
