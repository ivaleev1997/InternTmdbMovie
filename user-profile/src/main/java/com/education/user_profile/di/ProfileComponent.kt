package com.education.user_profile.di

import com.education.core.CoreProviderFactory
import com.education.core_api.di.CoreProvider
import com.education.core_api.presentation.viewmodel.ViewModelsProvider
import com.education.user_profile.presentation.UserProfileFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [UserProfileModule::class],
    dependencies = [CoreProvider::class, ViewModelsProvider::class]
)
interface ProfileComponent {

    companion object {
        fun create(coreProvider: CoreProvider): ProfileComponent {
            return DaggerProfileComponent
                .builder()
                .coreProvider(coreProvider)
                .viewModelsProvider(CoreProviderFactory.createViewModelProvider())
                .build()
        }
    }

    fun inject(userProfileFragment: UserProfileFragment)
}