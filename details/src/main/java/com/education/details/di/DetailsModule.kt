package com.education.details.di

import androidx.lifecycle.ViewModel
import com.education.core_api.di.module.SchedulersProviderModule
import com.education.core_api.extension.SchedulersProvider
import com.education.core_api.presentation.viewmodel.ViewModelTrigger
import com.education.details.data.DetailsRepository
import com.education.details.data.DetailsRepositoryImpl
import com.education.details.domain.DetailsUseCase
import com.education.details.presentation.DetailsViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [SchedulersProviderModule::class])
abstract class DetailsModule {

    @Binds
    abstract fun bindsRepository(detailsRepositoryImpl: DetailsRepositoryImpl): DetailsRepository

    @Module
    companion object {
        @Provides
        @JvmStatic
        @Singleton
        fun provideDetailsViewModel(
            map: @JvmSuppressWildcards MutableMap<Class<out ViewModel>, ViewModel>,
            detailsUseCase: DetailsUseCase,
            schedulersProvider: SchedulersProvider
        ): ViewModel = DetailsViewModel(
            detailsUseCase,
            schedulersProvider
        ).also {
            map[DetailsViewModel::class.java] = it
        }

        @Provides
        @JvmStatic
        @Singleton
        fun provideTrigger(viewModel: ViewModel) = ViewModelTrigger()
    }
}
