package com.education.login.di

import androidx.lifecycle.ViewModel
import com.education.core_api.extension.SchedulersProvider
import com.education.core_api.extension.SchedulersProviderImpl
import com.education.core_api.presentation.viewmodel.ViewModelTrigger
import com.education.login.data.repository.LoginRepository
import com.education.login.data.repository.LoginRepositoryImpl
import com.education.login.domain.UserUseCase
import com.education.login.presentation.LoginViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class LoginModule {

    @Binds
    abstract fun bindsLoginRepository(loginRepositoryImpl: LoginRepositoryImpl): LoginRepository

    @Binds
    abstract fun bindsSchedulersProvider(schedulersProviderImp: SchedulersProviderImpl): SchedulersProvider

    @Module
    companion object {

        @Provides
        @JvmStatic
        fun provideSchedulersProviderImpl(): SchedulersProviderImpl {
            return SchedulersProviderImpl
        }

        @Provides
        @JvmStatic
        @Singleton
        fun provideSongsListViewModel(
            map: @JvmSuppressWildcards MutableMap<Class<out ViewModel>, ViewModel>,
            userUseCase: UserUseCase,
            schedulersProvider: SchedulersProvider
        ): ViewModel = LoginViewModel(
            userUseCase,
            schedulersProvider
        ).also {
            map[LoginViewModel::class.java] = it
        }

        @Provides
        @JvmStatic
        @Singleton
        fun provideTrigger(viewModel: ViewModel) = ViewModelTrigger()
    }
}
