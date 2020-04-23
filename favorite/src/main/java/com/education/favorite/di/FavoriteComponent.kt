package com.education.favorite.di

import com.education.core.CoreProviderFactory
import com.education.core_api.di.CoreProvider
import com.education.core_api.presentation.viewmodel.ViewModelsProvider
import com.education.favorite.presentation.FavoriteFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [FavoriteModule::class],
    dependencies = [CoreProvider::class, ViewModelsProvider::class]
)
interface FavoriteComponent {

    companion object {
        fun create(coreProvider: CoreProvider): FavoriteComponent {
            return DaggerFavoriteComponent
                .builder()
                .coreProvider(coreProvider)
                .viewModelsProvider(CoreProviderFactory.createViewModelProvider())
                .build()
        }
    }

    fun inject(fragment: FavoriteFragment)
}
