package com.education.redmadrobottmdb.di.component

import android.app.Application
import com.education.core.CoreProviderFactory
import com.education.core_api.di.CoreProvider
import com.education.core_api.di.LocalDataSourceProvider
import com.education.core_api.di.NetworkProvider
import com.education.redmadrobottmdb.di.module.MediatorBindings
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [NetworkProvider::class, LocalDataSourceProvider::class],
    modules = [MediatorBindings::class]
)
interface CoreComponent : CoreProvider {

    companion object {

        private var coreComponent: CoreComponent? = null

        fun init(application: Application): CoreComponent {
            val appProvider = AppComponent.create(application)
            val localDataSourceProvider = CoreProviderFactory.createLocalDataSourceProvider(appProvider)
            return coreComponent
                ?: DaggerCoreComponent.builder()
                    .localDataSourceProvider(localDataSourceProvider)
                    .networkProvider(CoreProviderFactory.createNetworkProvider(localDataSourceProvider))
                    .build()
                    .also {
                        coreComponent = it
                    }
        }
    }
}
