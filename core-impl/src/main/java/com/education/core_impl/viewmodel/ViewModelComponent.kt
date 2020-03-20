package com.education.core_impl.viewmodel

import com.education.core_api.viewmodel.ViewModelsProvider
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [ViewModelModule::class]
)
interface ViewModelComponent : ViewModelsProvider