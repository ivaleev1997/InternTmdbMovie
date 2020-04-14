package com.education.core_impl.di.component

import com.education.core_api.di.AppProvider
import com.education.core_api.di.LocalDataSourceProvider
import com.education.core_impl.di.module.LocalDataSourceBinds
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [LocalDataSourceBinds::class],
    dependencies = [AppProvider::class]
)
@Singleton
interface LocalDataSourceComponent : LocalDataSourceProvider
