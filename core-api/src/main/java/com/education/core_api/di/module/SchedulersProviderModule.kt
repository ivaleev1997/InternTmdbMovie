package com.education.core_api.di.module

import com.education.core_api.extension.SchedulersProviderImpl
import dagger.Module
import dagger.Provides

@Module
class SchedulersProviderModule {

    @Provides
    fun provideSchedulersProvider(): SchedulersProviderImpl {
        return SchedulersProviderImpl
    }
}
