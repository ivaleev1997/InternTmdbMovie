package com.education.redmadrobottmdb.di.component

import com.education.core.CoreProviderFactory
import com.education.core_api.mediator.CoreProvider
import com.education.core_api.viewmodel.ViewModelsProvider
import com.education.redmadrobottmdb.MainActivity
import com.education.redmadrobottmdb.di.module.MainModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [MainModule::class],
    dependencies = [CoreProvider::class, ViewModelsProvider::class]
)
interface MainComponent {

    companion object {
        fun create(coreProvider: CoreProvider): MainComponent {
            return DaggerMainComponent.builder()
                .coreProvider(coreProvider)
                .viewModelsProvider(CoreProviderFactory.createViewModelProvider())
                .build()
        }
    }

    fun inject(mainActivity: MainActivity)
}
