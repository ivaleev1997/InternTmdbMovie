package com.education.favorite.di

import androidx.lifecycle.ViewModel
import com.education.core_api.di.module.SchedulersProviderModule
import com.education.core_api.extension.SchedulersProvider
import com.education.core_api.presentation.viewmodel.ViewModelTrigger
import com.education.favorite.data.FavoriteRepository
import com.education.favorite.data.FavoriteRepositoryImpl
import com.education.favorite.domain.FavoriteUseCase
import com.education.favorite.presentation.FavoriteViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [SchedulersProviderModule::class])
abstract class FavoriteModule {

    @Binds
    abstract fun bindsFavoriteRepository(favoriteRepositoryImpl: FavoriteRepositoryImpl): FavoriteRepository

    @Module
    companion object {
        @Provides
        @JvmStatic
        @Singleton
        fun provideFavoriteViewModel(
            map: @JvmSuppressWildcards MutableMap<Class<out ViewModel>, ViewModel>,
            favoriteUseCase: FavoriteUseCase,
            schedulersProvider: SchedulersProvider
        ): ViewModel = FavoriteViewModel(
            favoriteUseCase,
            schedulersProvider
        ).also {
            map[FavoriteViewModel::class.java] = it
        }

        @Provides
        @JvmStatic
        @Singleton
        fun provideTrigger(viewModel: ViewModel) = ViewModelTrigger()
    }
}
