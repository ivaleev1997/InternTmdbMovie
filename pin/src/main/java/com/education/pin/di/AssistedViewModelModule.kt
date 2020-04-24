package com.education.pin.di

import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Module

@AssistedModule
@Module(includes = [AssistedInject_AssistedViewModelModule::class])
internal abstract class AssistedViewModelModule