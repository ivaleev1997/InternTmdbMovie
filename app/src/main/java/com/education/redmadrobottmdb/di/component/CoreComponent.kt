package com.education.redmadrobottmdb.di.component

import android.app.Application
import com.education.core.CoreProviderFactory
import com.education.core_api.mediator.CoreProvider
import com.education.core_api.mediator.NetworkProvider
import com.education.core_api.mediator.SharedProvider
import com.education.redmadrobottmdb.di.module.MediatorBindings
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [NetworkProvider::class, SharedProvider::class],
    modules = [MediatorBindings::class]
)
interface CoreComponent : CoreProvider {

    companion object {

        private var appComponent: CoreComponent? = null

        fun init(application: Application): CoreComponent {
            return appComponent
                ?: DaggerCoreComponent.builder()
                .sharedProvider(
                    AppComponent.create(
                        application
                    )
                )
                .networkProvider(CoreProviderFactory.createNetworkProvider())
                .build()
                .also {
                    appComponent = it
                }
        }
    }
}
