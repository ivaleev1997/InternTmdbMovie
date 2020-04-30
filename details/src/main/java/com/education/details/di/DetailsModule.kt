package com.education.details.di

import com.education.core_api.di.module.SchedulersProviderModule
import com.education.details.data.DetailsRepository
import com.education.details.data.DetailsRepositoryImpl
import dagger.Binds
import dagger.Module

@Module(includes = [SchedulersProviderModule::class, AssistedViewModelModule::class])
abstract class DetailsModule {

    @Binds
    abstract fun bindsRepository(detailsRepositoryImpl: DetailsRepositoryImpl): DetailsRepository

}
