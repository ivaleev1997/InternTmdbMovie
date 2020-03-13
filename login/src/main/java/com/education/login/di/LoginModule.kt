package com.education.login.di

import androidx.lifecycle.ViewModel
import com.education.core_api.viewmodel.ViewModelTrigger
import com.education.login.usecase.UserUseCase
import com.education.login.viewmodel.LoginViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class LoginModule {

    @Provides
    @Singleton
    fun provideSongsListViewModel(
        map: @JvmSuppressWildcards MutableMap<Class<out ViewModel>, ViewModel>,
        userUseCase: UserUseCase
    ): ViewModel = LoginViewModel(userUseCase).also {
        map[LoginViewModel::class.java] = it
    }

    @Provides
    @Singleton
    fun provideTrigger(viewModel: ViewModel) = ViewModelTrigger()
}