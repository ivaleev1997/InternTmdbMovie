package com.education.details.di

import com.education.core_api.di.CoreProvider
import com.education.details.presentation.DetailsFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [DetailsModule::class],
    dependencies = [CoreProvider::class]
)
interface DetailsComponent {

    companion object {
        fun create(coreProvider: CoreProvider): DetailsComponent {
            return DaggerDetailsComponent
                .builder()
                .coreProvider(coreProvider)
                .build()
        }
    }

    fun inject(fragment: DetailsFragment)
}
