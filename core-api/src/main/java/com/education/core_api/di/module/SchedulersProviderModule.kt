package com.education.core_api.di.module

import com.education.core_api.extension.SchedulersProvider
import com.education.core_api.extension.SchedulersProviderImpl
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class SchedulersProviderModule {

    @Binds
    abstract fun bindsSchedulersProvider(schedulersProviderImp: SchedulersProviderImpl): SchedulersProvider

    @Module
    companion object {
        @Provides
        @JvmStatic
        fun provideSchedulersProvider(): SchedulersProviderImpl {
            return SchedulersProviderImpl
        }
    }
}
