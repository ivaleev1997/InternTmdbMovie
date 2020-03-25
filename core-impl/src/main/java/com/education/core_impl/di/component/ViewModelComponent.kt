package com.education.core_impl.di.component

import com.education.core_api.presentation.viewmodel.ViewModelsProvider
import com.education.core_impl.di.module.ViewModelModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [ViewModelModule::class]
)
interface ViewModelComponent : ViewModelsProvider
