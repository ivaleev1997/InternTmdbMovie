package com.education.core_impl

import com.education.core_api.mediator.NetworkProvider
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [NetworkModule::class]
)
@Singleton
interface NetworkComponent : NetworkProvider