package com.education.core_impl.di.component

import com.education.core_api.di.AppProvider
import com.education.core_api.di.NetworkProvider
import com.education.core_impl.di.module.NetworkModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [NetworkModule::class],
    dependencies = [AppProvider::class]
)
interface NetworkComponent : NetworkProvider
