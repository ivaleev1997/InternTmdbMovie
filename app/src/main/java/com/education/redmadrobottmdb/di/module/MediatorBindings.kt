package com.education.redmadrobottmdb.di.module

import com.education.core_api.ui.LoginMediator
import com.education.login.LoginMediatorImpl
import dagger.Binds
import dagger.Module

@Module
interface MediatorBindings {

    @Binds
    fun bindsLoginMediator(loginMediatorImpl: LoginMediatorImpl): LoginMediator
}
