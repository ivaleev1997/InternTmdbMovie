package com.education.redmadrobottmdb.di.module

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.education.core_api.viewmodel.ViewModelTrigger
import com.education.redmadrobottmdb.viewmodel.MainViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class MainModule {

    @Provides
    @Singleton
    fun provideSongsListViewModel(
        map: @JvmSuppressWildcards MutableMap<Class<out ViewModel>, ViewModel>,
        sharedPreferences: SharedPreferences
    ): ViewModel = MainViewModel(
        sharedPreferences
    ).also {
        map[MainViewModel::class.java] = it
    }

    @Provides
    @Singleton
    fun provideTrigger(viewModel: ViewModel) = ViewModelTrigger()
}