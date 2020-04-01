package com.education.user_profile.di

import androidx.lifecycle.ViewModel
import com.education.core_api.extension.SchedulersProvider
import com.education.core_api.extension.SchedulersProviderImpl
import com.education.core_api.presentation.viewmodel.ViewModelTrigger
import com.education.user_profile.data.repository.ProfileRepository
import com.education.user_profile.data.repository.ProfileRepositoryImpl
import com.education.user_profile.domain.ProfileUseCase
import com.education.user_profile.presentation.UserProfileViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class UserProfileModule {

    @Binds
    abstract fun bindsProfileRepository(profileRepositoryImpl: ProfileRepositoryImpl): ProfileRepository

    @Binds
    abstract fun bindsSchedulersProvider(schedulersProviderImp: SchedulersProviderImpl): SchedulersProvider

    @Module
    companion object {

        @Provides
        @JvmStatic
        fun provideSchedulersProvider(): SchedulersProviderImpl {
            return SchedulersProviderImpl
        }

        @Provides
        @JvmStatic
        @Singleton
        fun provideUserProfileViewModel(
            map: @JvmSuppressWildcards MutableMap<Class<out ViewModel>, ViewModel>,
            useCase: ProfileUseCase,
            schedulersProvider: SchedulersProvider
        ): ViewModel = UserProfileViewModel(
            useCase,
            schedulersProvider
        ).also {
            map[UserProfileViewModel::class.java] = it
        }

        @Provides
        @JvmStatic
        @Singleton
        fun provideViewModelTrigger(viewModel: ViewModel) = ViewModelTrigger()
    }
}