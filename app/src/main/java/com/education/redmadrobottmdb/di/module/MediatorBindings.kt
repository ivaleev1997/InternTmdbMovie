package com.education.redmadrobottmdb.di.module

import com.education.core_api.presentation.ui.LoginMediator
import com.education.login.presentation.LoginMediatorImpl
import dagger.Binds
import dagger.Module

@Module
interface MediatorBindings {

    @Binds
    fun bindsLoginMediator(loginMediatorImpl: LoginMediatorImpl): LoginMediator
}
