package com.education.details.di

import com.education.core.CoreProviderFactory
import com.education.core_api.di.CoreProvider
import com.education.core_api.presentation.viewmodel.ViewModelsProvider
import com.education.details.presentation.DetailsFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [DetailsModule::class],
    dependencies = [CoreProvider::class, ViewModelsProvider::class]
)
interface DetailsComponent {

    companion object {
        fun create(coreProvider: CoreProvider): DetailsComponent {
            return DaggerDetailsComponent
                .builder()
                .coreProvider(coreProvider)
                .viewModelsProvider(CoreProviderFactory.createViewModelProvider())
                .build()
        }
    }

    fun inject(fragment: DetailsFragment)
}