package com.education.pin.di

import com.education.core.CoreProviderFactory
import com.education.core_api.di.CoreProvider
import com.education.core_api.presentation.viewmodel.ViewModelsProvider
import com.education.pin.presentation.createpin.CreatePinFragment
import com.education.pin.presentation.enterpin.EnterPinFragment
import dagger.Component
import javax.inject.Singleton

@Component(
    dependencies = [CoreProvider::class, ViewModelsProvider::class],
    modules = [PinModule::class]
)
@Singleton
interface PinComponent {

    companion object {
        fun create(coreProvider: CoreProvider): PinComponent {
            return DaggerPinComponent
                .builder()
                .coreProvider(coreProvider)
                .viewModelsProvider(CoreProviderFactory.createViewModelProvider())
                .build()
        }
    }

    fun inject(fragment: CreatePinFragment)

    fun inject(fragment: EnterPinFragment)
}
