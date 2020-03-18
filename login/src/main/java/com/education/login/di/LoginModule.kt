package com.education.login.di

import androidx.lifecycle.ViewModel
import com.education.core_api.viewmodel.ViewModelTrigger
import com.education.login.repository.LoginRepository
import com.education.login.repository.LoginRepositoryImpl
import com.education.login.usecase.UserUseCase
import com.education.login.viewmodel.LoginViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class LoginModule {

    @Binds
    abstract fun bindsLoginRepository(loginRepositoryImpl: LoginRepositoryImpl): LoginRepository

    @Module
    companion object {
        @Provides
        @JvmStatic
        @Singleton
        fun provideSongsListViewModel(
            map: @JvmSuppressWildcards MutableMap<Class<out ViewModel>, ViewModel>,
            userUseCase: UserUseCase
        ): ViewModel = LoginViewModel(userUseCase).also {
            map[LoginViewModel::class.java] = it
        }

        @Provides
        @JvmStatic
        @Singleton
        fun provideTrigger(viewModel: ViewModel) = ViewModelTrigger()
    }
}
