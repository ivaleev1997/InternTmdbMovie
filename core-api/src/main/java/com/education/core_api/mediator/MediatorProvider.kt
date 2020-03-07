package com.education.core_api.mediator

import com.education.core_api.ui.LoginMediator

interface MediatorProvider {

    fun provideLoginMediator(): LoginMediator
}