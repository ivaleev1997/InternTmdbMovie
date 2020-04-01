package com.education.core_impl.di.module

import com.education.core_api.data.LocalDataSource
import com.education.core_impl.data.LocalDataSourceImpl
import dagger.Binds
import dagger.Module

@Module
abstract class LocalDataSourceBinds {

    @Binds
    abstract fun bindsLocalDataSource(localDataSourceImpl: LocalDataSourceImpl): LocalDataSource
}
