package com.education.movies.di

import com.education.core.CoreProviderFactory
import com.education.core_api.di.CoreProvider
import com.education.core_api.presentation.viewmodel.ViewModelsProvider
import com.education.movies.presentation.MoviesFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [MoviesModule::class],
    dependencies = [CoreProvider::class, ViewModelsProvider::class]
)
interface MoviesComponent {

    companion object {
        fun create(coreProvider: CoreProvider): MoviesComponent {
            return DaggerMoviesComponent
                .builder()
                .coreProvider(coreProvider)
                .viewModelsProvider(CoreProviderFactory.createViewModelProvider())
                .build()
        }
    }

    fun inject(moviesFragment: MoviesFragment)
}