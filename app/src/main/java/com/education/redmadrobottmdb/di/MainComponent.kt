package com.education.redmadrobottmdb.di

import com.education.core_api.mediator.ProvidersFacade
import com.education.redmadrobottmdb.MainActivity
import dagger.Component

@Component(
    dependencies = [ProvidersFacade::class]
)
interface MainComponent {

    companion object {
        fun create(providersFacade: ProvidersFacade): MainComponent {
            return DaggerMainComponent
                .builder()
                .providersFacade(providersFacade)
                .build()
        }
    }

    fun inject(mainActivity: MainActivity)
}