package com.education.login.di

import com.education.core.CoreProviderFactory
import com.education.core_api.di.CoreProvider
import com.education.core_api.presentation.viewmodel.ViewModelsProvider
import com.education.login.presentation.LoginFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [LoginModule::class],
    dependencies = [CoreProvider::class, ViewModelsProvider::class]
)
interface LoginComponent {
    companion object {
        fun create(coreProvider: CoreProvider): LoginComponent {
            return DaggerLoginComponent
                .builder()
                .coreProvider(coreProvider)
                .viewModelsProvider(CoreProviderFactory.createViewModelProvider())
                .build()
        }
    }

    fun inject(loginFragment: LoginFragment)
}
