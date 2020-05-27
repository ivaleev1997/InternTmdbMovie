package com.education.redmadrobottmdb.di.module

import androidx.lifecycle.ViewModel
import com.education.core_api.data.LocalDataSource
import com.education.core_api.presentation.viewmodel.ViewModelTrigger
import com.education.core_api.util.CurrentTime
import com.education.redmadrobottmdb.presentation.activity.MainViewModel
import dagger.Module
import dagger.Provides
import java.util.*
import javax.inject.Singleton

@Module
class MainModule {

    @Provides
    @Singleton
    fun provideSongsListViewModel(
        map: @JvmSuppressWildcards MutableMap<Class<out ViewModel>, ViewModel>,
        localDataSource: LocalDataSource,
        currentTime: CurrentTime
    ): ViewModel = MainViewModel(
        localDataSource,
        currentTime
    ).also {
        map[MainViewModel::class.java] = it
    }

    @Provides
    @Singleton
    fun provideTrigger(viewModel: ViewModel) = ViewModelTrigger()

    @Provides
    @Singleton
    fun provideCurrentTime() = object : CurrentTime {
        override fun getCurrentTimeMills(): Long {
            return Calendar.getInstance().time.time
        }

    }
}
