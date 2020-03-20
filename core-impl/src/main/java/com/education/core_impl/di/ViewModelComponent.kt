package com.education.core_impl.di

import com.education.core_api.presentation.viewmodel.ViewModelsProvider
import com.education.core_impl.presentation.viewmodel.ViewModelModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [ViewModelModule::class]
)
interface ViewModelComponent : ViewModelsProvider
