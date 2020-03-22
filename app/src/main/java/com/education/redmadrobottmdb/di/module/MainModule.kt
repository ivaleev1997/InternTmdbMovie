package com.education.redmadrobottmdb.di.module

import androidx.lifecycle.ViewModel
import com.education.core_api.data.LocalDataSource
import com.education.core_api.presentation.viewmodel.ViewModelTrigger
import com.education.redmadrobottmdb.presentation.MainViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class MainModule {

    @Provides
    @Singleton
    fun provideSongsListViewModel(
        map: @JvmSuppressWildcards MutableMap<Class<out ViewModel>, ViewModel>,
        localDataSource: LocalDataSource
    ): ViewModel = MainViewModel(
        localDataSource
    ).also {
        map[MainViewModel::class.java] = it
    }

    @Provides
    @Singleton
    fun provideTrigger(viewModel: ViewModel) = ViewModelTrigger()
}
