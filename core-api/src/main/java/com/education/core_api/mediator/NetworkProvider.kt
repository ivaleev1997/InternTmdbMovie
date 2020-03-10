package com.education.core_api.mediator

import com.education.core_api.network.NetworkContract

interface NetworkProvider {

    fun provideNetwork(): NetworkContract
}
