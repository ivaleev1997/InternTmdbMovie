package com.education.pin.di

import androidx.lifecycle.ViewModel
import com.education.core_api.di.module.SchedulersProviderModule
import com.education.core_api.extension.SchedulersProvider
import com.education.core_api.presentation.viewmodel.ViewModelTrigger
import com.education.pin.data.PinRepository
import com.education.pin.data.PinRepositoryImpl
import com.education.pin.domain.PinUseCase
import com.education.pin.presentation.enterpin.EnterPinViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module(includes = [SchedulersProviderModule::class, AssistedViewModelModule::class])
abstract class PinModule {

    @Binds
    abstract fun bindsPinRepository(pinRepositoryImpl: PinRepositoryImpl): PinRepository

    @Module
    companion object {
        @Provides
        @JvmStatic
        @Singleton
        @Named("EnterPinViewModel")
        fun provideEnterPinViewModel(
            map: @JvmSuppressWildcards MutableMap<Class<out ViewModel>, ViewModel>,
            pinUseCase: PinUseCase,
            schedulersProvider: SchedulersProvider
        ): ViewModel = EnterPinViewModel(
            pinUseCase,
            schedulersProvider
        ).also {
            map[EnterPinViewModel::class.java] = it
        }

        @Provides
        @JvmStatic
        @Singleton
        fun provideTriggerEnterPin(@Named("EnterPinViewModel")viewModel: ViewModel) = ViewModelTrigger()
    }
}
