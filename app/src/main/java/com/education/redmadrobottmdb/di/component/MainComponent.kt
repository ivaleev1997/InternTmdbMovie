package com.education.redmadrobottmdb.di.component

import com.education.core_api.mediator.CoreProvider
import com.education.redmadrobottmdb.MainActivity
import dagger.Component

@Component(
    dependencies = [CoreProvider::class]
)
interface MainComponent {

    companion object {
        fun create(coreProvider: CoreProvider): MainComponent {
            return DaggerMainComponent.builder()
                .coreProvider(coreProvider)
                .build()
        }
    }

    fun inject(mainActivity: MainActivity)
}
