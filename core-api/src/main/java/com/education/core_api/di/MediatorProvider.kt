package com.education.core_api.di

import com.education.core_api.presentation.ui.LoginMediator

interface MediatorProvider {

    fun provideLoginMediator(): LoginMediator
}
