package com.education.movies.di

import androidx.lifecycle.ViewModel
import com.education.core_api.di.module.SchedulersProviderModule
import com.education.core_api.extension.SchedulersProvider
import com.education.core_api.presentation.viewmodel.ViewModelTrigger
import com.education.movies.data.repository.MoviesRepository
import com.education.movies.data.repository.MoviesRepositoryImpl
import com.education.movies.domain.MoviesUseCase
import com.education.movies.presentation.MoviesViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [SchedulersProviderModule::class])
abstract class MoviesModule {

    @Binds
    abstract fun bindsMoviesRepository(moviesRepositoryImpl: MoviesRepositoryImpl): MoviesRepository

    @Module
    companion object {
        @Provides
        @JvmStatic
        @Singleton
        fun provideMoviesViewModel(
            map: @JvmSuppressWildcards MutableMap<Class<out ViewModel>, ViewModel>,
            moviesUseCase: MoviesUseCase,
            schedulersProvider: SchedulersProvider
        ): ViewModel = MoviesViewModel(
            moviesUseCase,
            schedulersProvider
        ).also {
            map[MoviesViewModel::class.java] = it
        }

        @Provides
        @JvmStatic
        @Singleton
        fun provideTrigger(viewModel: ViewModel) = ViewModelTrigger()
    }
}
